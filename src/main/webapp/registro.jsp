<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Registro - Manos a la Obra</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
  <div class="container py-5" style="max-width:480px">
    <h2 class="mb-4">Crear cuenta de ciudadano</h2>
    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
    <% } %>
    <form method="post" action="registro">
      <div class="mb-3"><label class="form-label">Nombre completo</label>
        <input name="nombre" class="form-control form-control-lg" required></div>
      <div class="mb-3"><label class="form-label">Correo</label>
        <input name="correo" type="email" class="form-control form-control-lg" required></div>
      <div class="mb-3"><label class="form-label">Teléfono</label>
        <input name="telefono" class="form-control form-control-lg"></div>
      <div class="mb-3"><label class="form-label">Contraseña (mínimo 8 caracteres)</label>
        <input name="clave" type="password" minlength="8" class="form-control form-control-lg" required></div>
      <button class="btn btn-primary btn-lg w-100">Registrarme</button>
    </form>
  </div>
</body>
</html>
