@regression @third_party_transfer
Feature: Transferencia a otras cuentas Los Andes

  Background:
    Given el usuario ha iniciado sesión

  Scenario: Transferencia a otra cuenta Los Andes por S/ 1.00
    When navego a Operaciones
    And selecciono Transferencias
    And selecciono A otras cuentas Los Andes
    Then la pantalla de ingreso de cuenta carga
    When ingreso el número de cuenta destino y continúo
    Then la pantalla de detalles carga
    When ingreso el monto "100" en transferencia a terceros y continúo
    Then la pantalla de resumen de transferencia a terceros carga
    When confirmo la transferencia a terceros
    Then el comprobante de transferencia a terceros aparece
