package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu base;
    private ProductoAjustado ajustado;

    @BeforeEach
    void setUp( ) throws Exception
    {
        base = new ProductoMenu( "corral", 14000 );
        ajustado = new ProductoAjustado( base );
    }

    @Test
    void testGetNombre( )
    {
        // el nombre debe ser el mismo del producto base
        assertEquals( "corral", ajustado.getNombre( ), "El nombre del producto ajustado no es el esperado." );
    }

    @Test
    void testGetPrecioSinAjustes( )
    {
        // sin ingredientes agregados el precio es el del producto base
        assertEquals( 14000, ajustado.getPrecio( ), "El precio sin ajustes no es el esperado." );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        ajustado.agregarIngrediente( new Ingrediente( "tomate", 1000 ) );
        ajustado.agregarIngrediente( new Ingrediente( "queso mozzarella", 2500 ) );
        assertEquals( 17500, ajustado.getPrecio( ), "El precio con ingredientes agregados no es el esperado." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        ajustado.agregarIngrediente( new Ingrediente( "tomate", 1000 ) );
        ajustado.agregarIngrediente( new Ingrediente( "queso mozzarella", 2500 ) );
        ajustado.eliminarIngrediente( new Ingrediente( "cebolla", 1000 ) );

        String esperado = "corral\n            14000\n" +
                "    +tomate                1000" +
                "    +queso mozzarella                2500" +
                "    -cebolla" +
                "            17500\n";

        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

}
