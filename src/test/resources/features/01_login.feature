@regression
Feature: Login en la aplicación Los Andes

  @login_test @login_mainframe
  Scenario: Login Exitoso
    Given estoy en la pantalla de bienvenida
    When ingreso el usuario "71313648" y la clave "140304"
    Then el resultado debería ser "exitoso"

  @login_test @smoke
  Scenario: Credenciales Inválidas
    Given estoy en la pantalla de bienvenida
    When ingreso el usuario "00000000" y la clave "000000"
    Then el resultado debería ser "error_modal"
