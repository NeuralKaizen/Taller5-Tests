package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private Restaurante restaurante;

    // archivos reales con la informacion del restaurante
    private File archivoIngredientes = new File( "data/ingredientes.txt" );
    private File archivoMenu = new File( "data/menu.txt" );
    private File archivoCombos = new File( "data/combos.txt" );

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
    }

    @Test
    void testRestauranteVacio( )
    {
        assertTrue( restaurante.getIngredientes( ).isEmpty( ), "Un restaurante nuevo no debe tener ingredientes." );
        assertTrue( restaurante.getMenuBase( ).isEmpty( ), "Un restaurante nuevo no debe tener productos en el menu." );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ), "Un restaurante nuevo no debe tener combos." );
        assertTrue( restaurante.getPedidos( ).isEmpty( ), "Un restaurante nuevo no debe tener pedidos." );
        assertNull( restaurante.getPedidoEnCurso( ), "Un restaurante nuevo no debe tener un pedido en curso." );
    }

    @Test
    void testCargarInformacion( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );

        assertEquals( 15, restaurante.getIngredientes( ).size( ), "No se cargaron todos los ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "No se cargaron todos los productos del menu." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "No se cargaron todos los combos." );
    }

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Calle 1" );

        assertNotNull( restaurante.getPedidoEnCurso( ), "Debe haber un pedido en curso." );
        assertEquals( "Juan", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El cliente del pedido en curso no es el esperado." );
    }

    @Test
    void testIniciarPedidoConOtroEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Calle 1" );

        // no se puede iniciar un pedido si ya hay uno en curso
        assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Ana", "Calle 2" ) );
    }

    @Test
    void testCerrarSinPedidoEnCurso( )
    {
        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ) );
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        // se asegura de que exista la carpeta donde se guardan las facturas
        new File( "./facturas/" ).mkdirs( );

        restaurante.iniciarPedido( "Juan", "Calle 1" );
        restaurante.getPedidoEnCurso( ).agregarProducto( new ProductoMenu( "corral", 14000 ) );
        int idPedido = restaurante.getPedidoEnCurso( ).getIdPedido( );

        restaurante.cerrarYGuardarPedido( );

        File factura = new File( "./facturas/factura_" + idPedido + ".txt" );
        assertTrue( factura.exists( ), "No se guardo el archivo de la factura." );
        assertNull( restaurante.getPedidoEnCurso( ), "Despues de cerrar no debe haber un pedido en curso." );
        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debe quedar en el historico." );

        factura.delete( );
    }

    @Test
    void testIngredienteRepetido( )
    {
        File malo = new File( "data/ingredientes_repetidos.txt" );
        assertThrows( IngredienteRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( malo, archivoMenu, archivoCombos ) );
    }

    @Test
    void testProductoRepetido( )
    {
        File malo = new File( "data/menu_repetido.txt" );
        assertThrows( ProductoRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( archivoIngredientes, malo, archivoCombos ) );
    }

    @Test
    void testComboRepetido( )
    {
        File malo = new File( "data/combos_repetidos.txt" );
        assertThrows( ProductoRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, malo ) );
    }

    @Test
    void testProductoFaltanteEnCombo( )
    {
        File malo = new File( "data/combos_faltante.txt" );
        assertThrows( ProductoFaltanteException.class, ( ) -> restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, malo ) );
    }

}
