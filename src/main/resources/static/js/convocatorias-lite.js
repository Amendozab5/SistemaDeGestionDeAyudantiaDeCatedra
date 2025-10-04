const initGestionConvocatoriasLite = () => {
    const tbody = document.getElementById('tbody-convocatorias');
    if (!tbody || tbody.dataset.inited === '1') {
        return;
    }
    tbody.dataset.inited = '1';

    console.log('[convocatorias-lite] Initializing...');

    // Placeholder para la función de carga
    const cargarConvocatorias = () => {
        console.log('Cargando convocatorias...');
        tbody.innerHTML = '<tr><td colspan="5" class="text-center p-4">Funcionalidad de carga no implementada en este paso.</td></tr>';
    };

    // Placeholder para el botón de nueva convocatoria
    const btnNueva = document.getElementById('btn-nueva-convocatoria');
    if(btnNueva) {
        btnNueva.addEventListener('click', () => {
            alert('Abrir drawer para nueva convocatoria (placeholder).');
        });
    }

    // Carga inicial de datos (placeholder)
    cargarConvocatorias();
};

document.addEventListener('DOMContentLoaded', () => {
    // Este listener es un fallback por si el script se carga en una página no dinámica.
    if (document.getElementById('tbody-convocatorias')) {
        initGestionConvocatoriasLite();
    }
});

document.addEventListener('convocatorias-loaded', initGestionConvocatoriasLite);
