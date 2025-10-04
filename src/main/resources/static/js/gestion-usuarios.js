const initGestionUsuarios = () => {
    const tbody = document.getElementById('tbody-usuarios');
    if (!tbody || tbody.dataset.inited === '1') {
        return;
    }
    tbody.dataset.inited = '1';

    console.log('[gestion] init for fragment', Date.now());

    let currentPage = 0;
    let currentUserId = null;
    const pageSize = 10;

    // --- Elementos ---
    const paginationBar = document.getElementById('pagination-bar');
    const q = document.getElementById('q');
    const fRol = document.getElementById('f-rol');
    const fEstado = document.getElementById('f-estado');

    // Drawer
    const drawerOverlay = document.getElementById('drawer-overlay');
    const drawerUsuario = document.getElementById('drawer-usuario');
    const drawerTitle = document.getElementById('drawer-title');
    const btnNuevoUsuario = document.getElementById('btn-nuevo-usuario');
    const btnCancelar = document.getElementById('btn-cancelar');
    const btnGuardar = document.getElementById('btn-guardar');
    const drawerCloseBtn = document.getElementById('drawer-close-btn');
    const formUsuario = document.getElementById('form-usuario');

    // --- Lógica de Búsqueda y Carga ---
    const debounce = (func, delay) => {
        let timeout;
        return (...args) => {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), delay);
        };
    };

    const cargarUsuarios = async (params = {}) => {
        const { q = '', rol = '', estado = '', page = 0 } = params;
        currentPage = page ?? 0;

        tbody.innerHTML = '<tr><td colspan="6" class="text-center p-3">Cargando…</td></tr>';

        try {
            const url = `/admin/users?q=${encodeURIComponent(q)}&rol=${encodeURIComponent(rol)}&estado=${encodeURIComponent(estado)}&page=${currentPage}&size=${pageSize}&sort=apellidos,asc`;
            const response = await fetch(url, {
                credentials: 'same-origin',
                headers: { 'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest' }
            });

            if (response.status === 401) {
                window.location.href = '/login';
                return;
            }
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            if (!data.content || data.content.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-muted">Sin resultados para los filtros actuales</td></tr>';
                renderPaginacion({ number: 0, totalPages: 0 });
            } else {
                renderTabla(data);
                renderPaginacion(data);
            }
        } catch (error) {
            console.error('Error al cargar usuarios:', error);
            tbody.innerHTML = `<tr><td colspan="6" class="text-center p-4 text-red-500">Error al cargar datos: ${error.message}</td></tr>`;
        }
    };

    const debouncedSearch = debounce(() => {
        cargarUsuarios({ q: q.value, rol: fRol.value, estado: fEstado.value, page: 0 });
    }, 400);

    // --- Renderizado ---
    const renderTabla = (page) => {
        tbody.innerHTML = '';
        page.content.forEach(user => {
            const tr = document.createElement('tr');
            const estadoClass = user.estado === 'ACTIVA' ? 'bg-green-100 text-green-800' : (user.estado === 'PENDIENTE' ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800');
            tr.innerHTML = `
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${user.nombre} ${user.apellido}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${user.email}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${user.roles.map(rol => `<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">${rol}</span>`).join(' ')}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${estadoClass}">${user.estado}</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${user.ultimoAcceso ? new Date(user.ultimoAcceso).toLocaleString() : 'Nunca'}</td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button class="text-indigo-600 hover:text-indigo-900 btn-edit" data-id="${user.id}">Editar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    };

    const renderPaginacion = (page) => {
        paginationBar.innerHTML = '';
        if (page.totalPages > 1) {
            const prevPageBtn = document.createElement('button');
            prevPageBtn.id = 'prev-page';
            prevPageBtn.textContent = 'Anterior';
            prevPageBtn.className = 'px-4 py-2 border rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50';
            prevPageBtn.disabled = page.first;
            prevPageBtn.addEventListener('click', () => cargarUsuarios({ q: q.value, rol: fRol.value, estado: fEstado.value, page: page.number - 1 }));

            const pageIndicator = document.createElement('div');
            pageIndicator.id = 'page-indicator';
            pageIndicator.className = 'text-sm text-gray-700';
            pageIndicator.textContent = `Página ${page.number + 1} de ${page.totalPages}`;

            const nextPageBtn = document.createElement('button');
            nextPageBtn.id = 'next-page';
            nextPageBtn.textContent = 'Siguiente';
            nextPageBtn.className = 'px-4 py-2 border rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50';
            nextPageBtn.disabled = page.last;
            nextPageBtn.addEventListener('click', () => cargarUsuarios({ q: q.value, rol: fRol.value, estado: fEstado.value, page: page.number + 1 }));
            
            paginationBar.appendChild(prevPageBtn);
            paginationBar.appendChild(pageIndicator);
            paginationBar.appendChild(nextPageBtn);
        }
    };

    // --- Lógica del Drawer ---
    const openDrawer = () => {
        drawerOverlay.classList.remove('hidden');
        drawerUsuario.classList.remove('translate-x-full');
    };

    const closeDrawer = () => {
        drawerOverlay.classList.add('hidden');
        drawerUsuario.classList.add('translate-x-full');
        formUsuario.reset();
        currentUserId = null;
    };

    const checkFormValidity = () => {
        btnGuardar.disabled = !formUsuario.checkValidity();
    };

    const handleSaveUser = async (e) => {
        e.preventDefault();
        if (!formUsuario.checkValidity()) return;

        const originalButtonText = btnGuardar.innerHTML;
        btnGuardar.disabled = true;
        btnGuardar.innerHTML = 'Guardando...';
        btnCancelar.disabled = true;

        const formData = new FormData(formUsuario);
        const data = {
            nombres: formData.get('nombres'),
            apellidos: formData.get('apellidos'),
            email: formData.get('email'),
            cedula: formData.get('cedula'),
            telefono: formData.get('telefono'),
            rol: formData.get('rol'),
            estado: formData.get('estado'),
            forzarCambioPwd: formData.has('forzarCambioPwd')
        };

        const url = currentUserId ? `/admin/users/${currentUserId}` : '/admin/users';
        const method = currentUserId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                body: JSON.stringify(data),
                credentials: 'same-origin'
            });

            if (response.status === 401) {
                window.location.href = '/login';
                return;
            }

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: 'Error desconocido en el servidor.' }));
                throw new Error(errorData.message || 'Ocurrió un error al guardar.');
            }

            const savedUser = await response.json();
            console.log('Usuario guardado:', savedUser.id);
            
            closeDrawer();
            alert('Usuario guardado con éxito');
            cargarUsuarios({ q: q.value, rol: fRol.value, estado: fEstado.value, page: currentPage });

        } catch (error) {
            console.error('Error al guardar usuario:', error);
            alert(error.message);
        } finally {
            btnGuardar.disabled = false;
            btnGuardar.innerHTML = originalButtonText;
            btnCancelar.disabled = false;
        }
    };

    // --- Event Listeners ---
    q.addEventListener('input', debouncedSearch);
    [fRol, fEstado].forEach(el => el.addEventListener('change', () => cargarUsuarios({ q: q.value, rol: fRol.value, estado: fEstado.value, page: 0 })));

    btnNuevoUsuario.addEventListener('click', () => {
        currentUserId = null;
        drawerTitle.textContent = 'Nuevo Usuario';
        formUsuario.reset();
        document.getElementById('estado').value = 'ACTIVA';
        checkFormValidity();
        openDrawer();
    });

    tbody.addEventListener('click', async (e) => {
        if (e.target.classList.contains('btn-edit')) {
            const userId = e.target.dataset.id;
            currentUserId = userId;
            drawerTitle.textContent = 'Editar Usuario';

            try {
                const response = await fetch(`/admin/users/${userId}`, { credentials: 'same-origin', headers: { 'Accept': 'application/json' } });
                if (response.status === 401) { window.location.href = '/login'; return; }
                if (!response.ok) { throw new Error('Error al cargar datos del usuario.'); }
                
                const user = await response.json();
                
                document.getElementById('nombres').value = user.nombre;
                document.getElementById('apellidos').value = user.apellido;
                document.getElementById('email').value = user.email;
                document.getElementById('cedula').value = user.cedula || '';
                document.getElementById('telefono').value = user.telefono || '';
                document.getElementById('rol').value = user.roles[0] || 'ESTUDIANTE';
                document.getElementById('estado').value = user.estado;
                document.getElementById('forzarCambioPwd').checked = false;

                checkFormValidity();
                openDrawer();
            } catch (error) {
                console.error('Error en modo edición:', error);
                alert(error.message);
            }
        }
    });

    formUsuario.addEventListener('submit', handleSaveUser);
    [drawerOverlay, btnCancelar, drawerCloseBtn].forEach(el => el.addEventListener('click', closeDrawer));
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && !drawerOverlay.classList.contains('hidden')) {
            closeDrawer();
        }
    });
    formUsuario.addEventListener('input', checkFormValidity);

    // --- Carga Inicial ---
    cargarUsuarios({ q: q.value || '', rol: fRol.value || '', estado: fEstado.value || '', page: 0 });
};

document.addEventListener('DOMContentLoaded', initGestionUsuarios);
document.addEventListener('gestion-usuarios-loaded', initGestionUsuarios);