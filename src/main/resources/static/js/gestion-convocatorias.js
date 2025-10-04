function fetchConvocatorias() {
    fetch('/convocatorias/all')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const tableBody = document.getElementById('convocatorias-table-body');
            if (!tableBody) return;

            tableBody.innerHTML = ''; // Clear existing data
            if (data.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="5" class="text-center py-4">No hay convocatorias creadas.</td></tr>';
                return;
            }

            data.forEach(convocatoria => {
                const row = document.createElement('tr');
                row.className = 'border-b border-gray-200 hover:bg-gray-100';

                const estadoText = convocatoria.estado ? 'Publicada' : 'Borrador';
                const estadoClasses = convocatoria.estado 
                    ? 'bg-green-100 text-green-800' 
                    : 'bg-yellow-100 text-yellow-800';

                row.innerHTML = `
                    <td class="py-3 px-5 text-left">
                        <div class="flex items-center">
                            <div class="ml-3">
                                <p class="text-gray-900 whitespace-no-wrap font-semibold">${convocatoria.titulo}</p>
                                <p class="text-gray-600 whitespace-no-wrap text-xs">${convocatoria.descripcion.substring(0, 50)}...</p>
                            </div>
                        </div>
                    </td>
                    <td class="py-3 px-5 text-left">
                        <p class="text-gray-900 whitespace-no-wrap">${convocatoria.periodo}</p>
                    </td>
                    <td class="py-3 px-5 text-left">
                        <p class="text-gray-900 whitespace-no-wrap">Inicio: ${convocatoria.fechaInicio}</p>
                        <p class="text-gray-600 whitespace-no-wrap">Fin: ${convocatoria.fechaFin}</p>
                    </td>
                    <td class="py-3 px-5 text-left">
                        <span class="relative inline-block px-3 py-1 font-semibold text-xs leading-tight ${estadoClasses} rounded-full">
                            <span class="relative">${estadoText}</span>
                        </span>
                    </td>
                    <td class="py-3 px-5 text-left">
                        <button class="bg-blue-500 hover:bg-blue-700 text-white text-xs font-bold py-1 px-2 rounded btn-editar" data-id="${convocatoria.idConvocatoria}">Editar</button>
                        <button class="bg-red-500 hover:bg-red-700 text-white text-xs font-bold py-1 px-2 rounded ml-2" onclick="eliminarConvocatoria(${convocatoria.idConvocatoria})">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching convocatorias:', error);
            const tableBody = document.getElementById('convocatorias-table-body');
            if(tableBody) tableBody.innerHTML = '<tr><td colspan="5" class="text-center py-4 text-red-500">Error al cargar las convocatorias.</td></tr>';
        });
}

function eliminarConvocatoria(id) {
    if (confirm('¿Estás seguro de que quieres eliminar esta convocatoria?')) {
        fetch(`/convocatorias/delete/${id}`, { 
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                fetchConvocatorias(); // Refresh the list
            } else {
                alert('Error al eliminar la convocatoria.');
            }
        })
        .catch(error => console.error('Error eliminando convocatoria:', error));
    }
}

// Run the fetch function immediately when the script is loaded.
fetchConvocatorias();