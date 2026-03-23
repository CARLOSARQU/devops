# language: es
@regression
Característica: Login en la aplicación Los Andes

  @login_test @login_mainframe
  Escenario: Login Exitoso
    Dado estoy en la pantalla de bienvenida
    Cuando ingreso el usuario "71313648" y la clave "140304"
    Entonces el resultado debería ser "exitoso"

  @login_test @smoke
  Escenario: Credenciales Inválidas
    Dado estoy en la pantalla de bienvenida
    Cuando ingreso el usuario "00000000" y la clave "000000"
    Entonces el resultado debería ser "error_modal"
