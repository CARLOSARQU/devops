# language: es
Característica: Pantalla Home de la aplicación Los Andes

  Antecedentes:
    Dado el usuario ha iniciado sesión

  Escenario: La pantalla Home carga correctamente
    Entonces la sección Mis productos es visible

  Escenario: Los accesos rápidos están visibles
    Entonces los shortcuts de Transferir, Transferencia Celular, Pagar Cuota y Abrir Cuenta Digital son visibles

  Escenario: El toggle de saldo alterna sin romper la pantalla
    Cuando alterno el toggle de saldo
    Entonces la pantalla Home sigue mostrándose correctamente
