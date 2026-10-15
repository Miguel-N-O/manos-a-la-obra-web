const formulario = document.getElementById("formularioLogin");
const cuadroError = document.getElementById("error");
const botonEntrar = document.getElementById("botonEntrar");

if (new URLSearchParams(window.location.search).get("registro") === "ok") {
  document.getElementById("cuentaCreada").classList.remove("d-none");
}

function mostrarError(mensaje) {
  cuadroError.textContent = mensaje;
  cuadroError.classList.remove("d-none");
}

async function iniciarSesion(evento) {
  evento.preventDefault();
  cuadroError.classList.add("d-none");
  botonEntrar.disabled = true;
  try {
    const respuesta = await fetch("/api/sesion", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        correo: formulario.correo.value,
        clave: formulario.clave.value
      })
    });
    const cuerpo = await respuesta.json();
    if (!respuesta.ok) {
      mostrarError(cuerpo.mensajes.join(" "));
      return;
    }
    const rol = cuerpo.datos[0].rol.nombre;
    const paginas = { TECNICO: "/tecnico/inicio.html", ADMIN: "/admin/inicio.html" };
    window.location.href = paginas[rol] || "/ciudadano/inicio.html";
  } catch (error) {
    mostrarError("No pudimos conectarnos con el servidor. Intenta de nuevo en unos minutos.");
  } finally {
    botonEntrar.disabled = false;
  }
}

formulario.addEventListener("submit", iniciarSesion);
