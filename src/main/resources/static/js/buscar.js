const contenedorResultados = document.getElementById("resultados");
const cuadroMensaje = document.getElementById("mensaje");
const formatoPesos = new Intl.NumberFormat("es-CO", {
  style: "currency",
  currency: "COP",
  maximumFractionDigits: 0
});

function mostrarMensaje(texto, tipo) {
  cuadroMensaje.textContent = texto;
  cuadroMensaje.className = "alert alert-" + tipo;
}

function ocultarMensaje() {
  cuadroMensaje.className = "alert d-none";
}

function crearLinea(etiqueta, valor) {
  const linea = document.createElement("div");
  const negrita = document.createElement("strong");
  negrita.textContent = etiqueta + ": ";
  linea.appendChild(negrita);
  linea.appendChild(document.createTextNode(valor));
  return linea;
}

function describirPromedio(promedio) {
  const valor = Number(promedio);
  return valor > 0 ? "★ " + valor.toFixed(1) : "Sin calificaciones aún";
}

function crearTarjeta(tecnico) {
  const columna = document.createElement("div");
  columna.className = "col";
  const tarjeta = document.createElement("div");
  tarjeta.className = "card h-100 shadow-sm";
  const cuerpo = document.createElement("div");
  cuerpo.className = "card-body d-flex flex-column";

  const nombre = document.createElement("h2");
  nombre.className = "h5 card-title";
  nombre.textContent = tecnico.usuario.nombre;
  cuerpo.appendChild(nombre);
  cuerpo.appendChild(crearLinea("Especialidad", tecnico.servicio.nombre));
  cuerpo.appendChild(crearLinea("Barrio", tecnico.barrio.nombre));
  cuerpo.appendChild(crearLinea("Tarifa", formatoPesos.format(tecnico.tarifa)));
  cuerpo.appendChild(crearLinea("Calificación", describirPromedio(tecnico.promedio)));

  const enlace = document.createElement("a");
  enlace.className = "btn btn-primary btn-lg mt-auto";
  enlace.href = "/ciudadano/tecnico.html?id=" + encodeURIComponent(tecnico.id);
  enlace.textContent = "Ver perfil";
  const pie = document.createElement("div");
  pie.className = "mt-3 d-grid";
  pie.appendChild(enlace);
  cuerpo.appendChild(pie);

  tarjeta.appendChild(cuerpo);
  columna.appendChild(tarjeta);
  return columna;
}

async function buscarTecnicos(parametros) {
  ocultarMensaje();
  contenedorResultados.replaceChildren();
  try {
    const respuesta = await fetch("/api/tecnicos?" + parametros.toString());
    if (respuesta.status === 401) {
      window.location.href = "/login.html";
      return;
    }
    const cuerpo = await respuesta.json();
    if (!respuesta.ok) {
      mostrarMensaje(cuerpo.mensajes.join(" "), "danger");
      return;
    }
    if (cuerpo.datos.length === 0) {
      mostrarMensaje(cuerpo.mensajes.join(" "), "info");
      return;
    }
    cuerpo.datos.forEach((tecnico) => contenedorResultados.appendChild(crearTarjeta(tecnico)));
  } catch (error) {
    mostrarMensaje("No pudimos conectarnos con el servidor. Intenta de nuevo en unos minutos.", "danger");
  }
}

buscarTecnicos(new URLSearchParams());
