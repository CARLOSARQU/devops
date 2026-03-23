@regression
Feature: Pantalla Home de la aplicación Los Andes

  Background:
    Given el usuario ha iniciado sesión

  @login_mainframe
  Scenario: La pantalla Home carga correctamente
    Then la sección Mis productos es visible

  @smoke
  Scenario: Los accesos rápidos están visibles
    Then los shortcuts de Transferir, Transferencia Celular, Pagar Cuota y Abrir Cuenta Digital son visibles

  @smoke
  Scenario: El toggle de saldo alterna sin romper la pantalla
    When alterno el toggle de saldo
    Then la pantalla Home sigue mostrándose correctamente
