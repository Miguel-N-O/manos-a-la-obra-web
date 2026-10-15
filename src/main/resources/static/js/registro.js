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
    indicativoPais: { id: formulario.indicativoPais.value },
    numeroTelefono: formulario.numeroTelefono.value,
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

async function cargarIndicativos() {
  try {
    const respuesta = await fetch("/api/indicativos-pais");
    const cuerpo = await respuesta.json();
    if (!respuesta.ok) {
      mostrarError(cuerpo.mensajes.join(" "));
      return;
    }
    const lista = formulario.indicativoPais;
    cuerpo.datos.forEach((item) => {
      const opcion = document.createElement("option");
      opcion.value = item.id;
      opcion.textContent = item.pais + " (+" + item.indicativo + ")";
      if (item.pais === "Colombia") {
        opcion.selected = true;
      }
      lista.appendChild(opcion);
    });
  } catch (error) {
    mostrarError("No pudimos cargar los indicativos de país. Recarga la página.");
  }
}

formulario.numeroTelefono.addEventListener("input", () => {
  formulario.numeroTelefono.value = formulario.numeroTelefono.value.replace(/\D/g, "");
});
formulario.addEventListener("submit", registrarCiudadano);
cargarIndicativos();
