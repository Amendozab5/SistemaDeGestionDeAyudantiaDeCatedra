(() => {
    console.log('[AUDITORIA] Intentando iniciar el script de auditoría...');
    const container = document.getElementById('auditoria-panel');
    if (!container) {
        console.log('[AUDITORIA] ERROR: Contenedor #auditoria-panel no encontrado.');
        return;
    }
    console.log('[AUDITORIA] OK: Contenedor encontrado y script iniciado.');

    const tabs = container.querySelectorAll('.audit-tab');
    const table = container.querySelector('#audit-table');
    const tableHead = container.querySelector('#audit-table-head');
    const tableBody = container.querySelector('#audit-table-body');
    const placeholder = container.querySelector('#audit-placeholder');
    
    const startDateInput = container.querySelector('#filter-start-date');
    const endDateInput = container.querySelector('#filter-end-date');
    const userInput = container.querySelector('#filter-user');
    const typeInput = container.querySelector('#filter-type');
    const applyFiltersBtn = container.querySelector('#btn-apply-filters');
    const clearFiltersBtn = container.querySelector('#btn-clear-filters');

    let activeEndpoint = null;

    if(!table || !tableHead || !tableBody || !placeholder || !startDateInput || !endDateInput || !userInput || !typeInput || !applyFiltersBtn || !clearFiltersBtn) {
        console.error('[AUDITORIA] ERROR: No se encontraron todos los elementos de la UI.');
        return;
    }
    console.log('[AUDITORIA] OK: Todos los elementos de la UI fueron encontrados.');

    const fetchAuditData = async (endpoint, params = {}) => {
        activeEndpoint = endpoint;
        const { startDate, endDate, user, type } = params;
        
        const url = new URL(endpoint, window.location.origin);
        if (startDate) url.searchParams.append('startDate', startDate);
        if (endDate) url.searchParams.append('endDate', endDate);
        if (user) url.searchParams.append('usuario', user);
        if (type) url.searchParams.append('tipo', type);

        console.log(`[AUDITORIA] Fetching data from: ${url.toString()}`);
        placeholder.classList.add('hidden');
        table.classList.add('hidden');
        tableHead.innerHTML = '';
        tableBody.innerHTML = '<tr><td colspan="100%" class="text-center p-4">Cargando...</td></tr>';
        table.classList.remove('hidden');

        try {
            const response = await fetch(url, {
                credentials: 'same-origin',
                headers: { 'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest' }
            });

            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }
            const data = await response.json();
            renderTable(data);

        } catch (error) {
            console.error('[AUDITORIA] ERROR al cargar datos:', error);
            tableBody.innerHTML = `<tr><td colspan="100%" class="text-center p-4 text-red-500">Error al cargar datos.</td></tr>`;
        }
    };

    const renderTable = (data) => {
        tableHead.innerHTML = '';
        tableBody.innerHTML = '';

        if (!data || data.length === 0) {
            table.classList.add('hidden');
            placeholder.textContent = 'No hay registros para esta categoría o filtros.';
            placeholder.classList.remove('hidden');
            return;
        }

        const headers = Object.keys(data[0]);
        const headerRow = document.createElement('tr');
        headers.forEach(header => {
            const th = document.createElement('th');
            th.className = 'px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider';
            th.textContent = header.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase());
            headerRow.appendChild(th);
        });
        tableHead.appendChild(headerRow);

        data.forEach(item => {
            const row = document.createElement('tr');
            row.className = 'hover:bg-gray-50';
            headers.forEach(header => {
                const cell = document.createElement('td');
                cell.className = 'px-6 py-4 whitespace-nowrap text-sm text-gray-700';
                let value = item[header];

                if (typeof value === 'object' && value !== null) {
                    value = JSON.stringify(value, null, 2);
                    cell.innerHTML = `<pre class="whitespace-pre-wrap">${value}</pre>`;
                } else {
                    cell.textContent = value;
                }
                row.appendChild(cell);
            });
            tableBody.appendChild(row);
        });

        table.classList.remove('hidden');
    };

    // --- Lógica de Eventos Corregida ---
    tabs.forEach(tab => {
        tab.addEventListener('click', (e) => {
            const endpoint = e.currentTarget.dataset.endpoint;
            activeEndpoint = endpoint; // Guardar el endpoint actual
            
            tabs.forEach(t => t.classList.remove('bg-blue-500', 'text-white'));
            e.currentTarget.classList.add('bg-blue-500', 'text-white');
            
            // Al hacer clic en una pestaña, se dispara el filtro automáticamente
            applyFiltersBtn.click();
        });
    });

    applyFiltersBtn.addEventListener('click', () => {
        if (activeEndpoint) {
            const params = {
                startDate: startDateInput.value,
                endDate: endDateInput.value,
                user: userInput.value,
                type: typeInput.value
            };
            fetchAuditData(activeEndpoint, params);
        } else {
            placeholder.textContent = 'Por favor, seleccione primero una categoría de auditoría.';
            placeholder.classList.remove('hidden');
        }
    });

    clearFiltersBtn.addEventListener('click', () => {
        startDateInput.value = '';
        endDateInput.value = '';
        userInput.value = '';
        typeInput.value = '';
        // Vuelve a disparar el filtro (ahora con los campos vacíos)
        applyFiltersBtn.click();
    });

    console.log('[AUDITORIA] OK: Listeners de clic añadidos.');
})();