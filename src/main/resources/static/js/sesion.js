// Encabezado compartido de las páginas internas: muestra el nombre y permite cerrar sesión.
(async function () {
  const nombre = document.getElementById("nombreUsuario");
  const botonSalir = document.getElementById("botonSalir");
  try {
    const respuesta = await fetch("/api/sesion");
    if (!respuesta.ok) {
      window.location.href = "/login.html";
      return;
    }
    const cuerpo = await respuesta.json();
    nombre.textContent = cuerpo.datos[0].nombre;
  } catch (error) {
    nombre.textContent = "";
  }
  botonSalir.addEventListener("click", async () => {
    await fetch("/api/sesion", { method: "DELETE" });
    window.location.href = "/login.html";
  });
})();
