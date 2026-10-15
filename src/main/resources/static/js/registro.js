const formulario = document.getElementById("formularioRegistro");
const botonRegistrar = document.getElementById("botonRegistrar");
const mensajeError = document.getElementById("mensajeError");

function mostrarError(texto) {
  mensajeError.textContent = texto;
  mensajeError.classList.remove("d-none");
}

function ocultarError() {
  mensajeError.classList.add("d-none");
}

function leerFormulario() {
  return {
    nombre: formulario.nombre.value,
    correo: formulario.correo.value,
    telefono: formulario.telefono.value,
    clave: formulario.clave.value
  };
}

async function registrarCiudadano(evento) {
  evento.preventDefault();
  ocultarError();
  botonRegistrar.disabled = true;
  try {
    const respuesta = await fetch("/api/usuarios/ciudadanos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(leerFormulario())
    });
    const cuerpo = await respuesta.json();
    if (respuesta.ok) {
      window.location.href = "index.html?registro=ok";
      return;
    }
    mostrarError(cuerpo.mensajes.join(" "));
  } catch (error) {
    mostrarError("No pudimos conectarnos con el servidor. Intenta de nuevo en unos minutos.");
  } finally {
    botonRegistrar.disabled = false;
  }
}

formulario.addEventListener("submit", registrarCiudadano);
