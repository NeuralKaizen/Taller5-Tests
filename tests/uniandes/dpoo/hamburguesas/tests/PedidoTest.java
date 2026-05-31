package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido = new Pedido( "Juan", "Calle 1" );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Juan", pedido.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testGetIdPedido( )
    {
        // los identificadores deben ser consecutivos entre dos pedidos seguidos
        Pedido otro = new Pedido( "Ana", "Calle 2" );
        assertEquals( pedido.getIdPedido( ) + 1, otro.getIdPedido( ), "Los identificadores de los pedidos no son consecutivos." );
    }

    @Test
    void testPrecios( )
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas medianas", 5500 ) );

        // neto = 19500, iva = 19500 * 0.19 = 3705, total = 23205
        assertEquals( 23205, pedido.getPrecioTotalPedido( ), "El precio total del pedido no es el esperado." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas medianas", 5500 ) );

        String esperado = "Cliente: Juan\n" +
                "Dirección: Calle 1\n" +
                "----------------\n" +
                "corral\n            14000\n" +
                "papas medianas\n            5500\n" +
                "----------------\n" +
                "Precio Neto:  19500\n" +
                "IVA:          3705\n" +
                "Precio Total: 23205\n";

        assertEquals( esperado, pedido.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );

        File archivo = File.createTempFile( "factura_prueba", ".txt" );
        pedido.guardarFactura( archivo );

        // se lee lo que quedo en el archivo y se compara con el texto de la factura
        StringBuffer contenido = new StringBuffer( );
        BufferedReader br = new BufferedReader( new FileReader( archivo ) );
        String linea = br.readLine( );
        while( linea != null )
        {
            contenido.append( linea + "\n" );
            linea = br.readLine( );
        }
        br.close( );
        archivo.delete( );

        assertEquals( pedido.generarTextoFactura( ), contenido.toString( ), "El contenido del archivo de la factura no es el esperado." );
    }

}
