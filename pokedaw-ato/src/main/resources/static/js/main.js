/**
 * Controlador Principal de la Aplicación (js/main.js)
 * PLANTILLA BASE EN BLANCO PARA EL ALUMNO
 *
 * Instrucciones para el alumno:
 * 1. En handleGuardar() construye el payload con los IDs del formulario
 * 2. En el filtro del buscador, ajusta los campos por los que quieres filtrar
 * 3. Si el service impide enviar algún campo (estado, fechaCreacion...), no lo incluyas en el payload
 * 4. Recuerda: en PUT el campo 'id' del body DEBE coincidir con el id de la URL
 */

import { ApiService } from './api.js';
import { UIService } from './ui.js';

// Cache local del listado para el buscador
let datosCache = [];

document.addEventListener('DOMContentLoaded', () => {
    initApp();
});

// -----------------------------------------------------------------------
// INICIALIZACIÓN
// -----------------------------------------------------------------------

async function initApp() {
    setupEventListeners();
    await cargarDatos();
}

// -----------------------------------------------------------------------
// CARGA DE DATOS
// -----------------------------------------------------------------------

async function cargarDatos() {
    try {
        UIService.showLoading();
        datosCache = await ApiService.getAll();
        UIService.renderTabla(datosCache);
    } catch (error) {
        console.error('Error al cargar datos:', error);
        UIService.showToast(error.message, 'error');
    }
}

// -----------------------------------------------------------------------
// EVENTOS
// -----------------------------------------------------------------------

function setupEventListeners() {

    // 1. Abrir modal para CREAR
    document.getElementById('btn-nuevo').addEventListener('click', () => {
        UIService.openModalForm(null);
    });

    // 2. Cerrar modal formulario
    document.getElementById('btn-cerrar-form').addEventListener('click', () => UIService.closeModalForm());
    document.getElementById('btn-cancelar-form').addEventListener('click', () => UIService.closeModalForm());

    // 3. Cerrar modal detalle
    document.getElementById('btn-cerrar-detalle').addEventListener('click', () => UIService.closeModalDetalle());
    document.getElementById('btn-cerrar-detalle-footer').addEventListener('click', () => UIService.closeModalDetalle());

    // 4. Recargar datos
    document.getElementById('btn-recargar').addEventListener('click', () => cargarDatos());

    // 5. Envío del formulario (Crear o Editar)
    document.getElementById('form-entidad').addEventListener('submit', async (e) => {
        e.preventDefault();
        await handleGuardar();
    });

    // 6. Buscador local en tiempo real
    document.getElementById('input-buscar').addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        if (!query) {
            UIService.renderTabla(datosCache);
            return;
        }
        // [ALUMNO] Ajusta los campos por los que quieres filtrar
		const filtrados = datosCache.filter(item => {
		    const coincideNombre = item.titulo && item.titulo.toLowerCase().includes(query);
		    const coincideTipo1 = item.tipo1 && item.tipo1.toLowerCase().includes(query);
		    const coincideTipo2 = item.tipo2 && item.tipo2.toLowerCase().includes(query);
		    const coincideNumero = item.numeroPokedex && String(item.numeroPokedex).includes(query);
		    return coincideNombre || coincideTipo1 || coincideTipo2 || coincideNumero;
		});
        UIService.renderTabla(filtrados);
    });

    // 7. Acciones de la tabla (delegación de eventos)
    document.getElementById('tabla-tbody').addEventListener('click', async (e) => {

        const btnVer = e.target.closest('.btn-ver');
        if (btnVer) {
            await handleVerDetalle(btnVer.dataset.id);
            return;
        }

        const btnEditar = e.target.closest('.btn-editar');
        if (btnEditar) {
            await handleEditar(btnEditar.dataset.id);
            return;
        }

        const btnBorrar = e.target.closest('.btn-borrar');
        if (btnBorrar) {
            await handleBorrar(btnBorrar.dataset.id);
            return;
        }
    });
}

// -----------------------------------------------------------------------
// ACCIONES CRUD
// -----------------------------------------------------------------------

/**
 * GUARDAR (POST si no hay id, PUT si hay id)
 * [ALUMNO] Construir el payload con los valores de los inputs del formulario
 */
async function handleGuardar() {
	const id = document.getElementById('id-pokemon').value;
	
	
	const numeroPokedex = document.getElementById('numero-pokedex').value;
	const titulo = document.getElementById('nombre-pokemon').value.trim();
	const tipo1 = document.getElementById('tipo1-pokemon').value;
	let tipo2 = document.getElementById('tipo2-pokemon').value;
	const capturado = document.getElementById('pokeball-pokemon').value;

    try {
        if (id) {
            // MODO EDICIÓN: PUT /entidad/{id}
            // ⚠️ El campo 'id' del body DEBE coincidir con el id de la URL
            // ⚠️ No incluir campos que el service no permita modificar
            const payloadUpdate = {
				id: parseInt(id),
				
				numeroPokedex: numeroPokedex,
				titulo: titulo,
				tipo1: tipo1,
				tipo2: tipo2,
				capturado: capturado,
				fechaCaptura: null
            };
            await ApiService.update(id, payloadUpdate);
            UIService.showToast(`Pokémon #${id} actualizado correctamente.`, 'success');
        } else {
            // MODO CREACIÓN: POST /entidad
            // ⚠️ No incluir campos que el service asigne automáticamente (estado, fechaCreacion...)
            const payloadCreate = {
				numeroPokedex: numeroPokedex,
				titulo: titulo,
				tipo1: tipo1,
				tipo2: tipo2,
				capturado: capturado,
				fechaCaptura: null
            };
            const nuevo = await ApiService.create(payloadCreate);
            UIService.showToast(`Pokémon #${nuevo.id} capturado con éxito.`, 'success');
        }

        UIService.closeModalForm();
        await cargarDatos();

    } catch (error) {
        console.error('Error al guardar:', error);
        UIService.showToast(error.message, 'error');
    }
}

/**
 * VER DETALLE → abre modal de detalle con todos los campos
 */
async function handleVerDetalle(id) {
    try {
        const item = await ApiService.getById(id);
        UIService.openModalDetalle(item);
    } catch (error) {
        console.error('Error al cargar detalle:', error);
        UIService.showToast(error.message, 'error');
    }
}

/**
 * EDITAR → obtiene el registro y abre el formulario en modo Edición
 */
async function handleEditar(id) {
    try {
        const item = await ApiService.getById(id);
        UIService.openModalForm(item);
    } catch (error) {
        console.error('Error al cargar datos para editar:', error);
        UIService.showToast(error.message, 'error');
    }
}

/**
 * BORRAR → pide confirmación y llama a DELETE
 */
async function handleBorrar(id) {
    if (!confirm(`¿Estás seguro de que deseas liberar al Pokémon #${id}?`)) return;

    try {
        await ApiService.delete(id);
        UIService.showToast(`Pokémon #${id} liberado correctamente.`, 'success');
        await cargarDatos();
    } catch (error) {
        console.error('Error al liberar:', error);
        UIService.showToast(error.message, 'error');
    }
}
