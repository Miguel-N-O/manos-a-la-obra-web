const parametros = new URLSearchParams(window.location.search);

if (parametros.get("registro") === "ok") {
  document.getElementById("cuentaCreada").classList.remove("d-none");
}
