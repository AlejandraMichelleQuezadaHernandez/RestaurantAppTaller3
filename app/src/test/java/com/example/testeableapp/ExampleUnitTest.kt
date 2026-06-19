package com.example.testeableapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {
    private lateinit var viewModel: RestaurantViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Configurar el Main Dispatcher antes de inicializar el ViewModel
        Dispatchers.setMain(testDispatcher)
        viewModel = RestaurantViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Test: Agregar item al pedido.
     */
    @Test
    fun `Add item to order updates quantities sucessfully`() = runTest {
        val idTest = 1
        viewModel.addItem(idTest)
        
        val currentState = viewModel.quantities.value
        assertEquals(1, currentState[idTest])
    }

    /**
     * Test: Incrementar cantidad
     */
    @Test
    fun `Increment quantity of items`() = runTest {
        val idTest = 1
        viewModel.addItem(idTest)
        viewModel.incrementItem(idTest)

        val currentState = viewModel.quantities.value
        assertEquals(2, currentState[idTest])
    }

    /**
     * Test: Decrementar cantidad
     */
    @Test
    fun `Decrement quantity when item id more than 1`() = runTest {
        val idTest = 2
        viewModel.addItem(idTest)
        viewModel.addItem(idTest)
        viewModel.decrementItem(idTest)

        val currentState = viewModel.quantities.value
        assertEquals(1, currentState[idTest])
    }

    /**
     * Test: Eliminar item al decrementar desde 1
     */
    @Test
    fun `Delete when item decrements quantity to 1`() = runTest {
        val idTest = 1
        viewModel.addItem(idTest)
        viewModel.decrementItem(idTest)

        val currentState = viewModel.quantities.value
        assertNull(currentState[idTest])
    }

    /**
     * Test: Cálculo del total a pagar
     */
    @Test
    fun `Calculate total of items to pay`() = runTest {
        val collectJob = launch { viewModel.total.collect {} }

        val idTest = 1
        val idTest2 = 2
        
        viewModel.addItem(idTest)
        viewModel.addItem(idTest2)

        runCurrent()

        val currentState = viewModel.total.value
        assertEquals(11.50, currentState, 0.0)
        
        collectJob.cancel()
    }

    /**
     * Test Adicional 1: No se puede hacer un pedido si el carrito está vacío,
     * es necesario este test ya que se debe hacer la validación donde no permita
     * a la persona hacer compras sin tener nada en carrito.
     */
    @Test
    fun `Place order with empty cart does not generate confirmation`() {

        viewModel.placeOrder()

        val currentConfirmation = viewModel.confirmation.value
        assertNull(currentConfirmation)
    }

    /**
     * Test Adicional 2: Cerrar la confirmación vacía los artículos
     * Este test es necesario porque asegura una buena experiencia de usuario.
     * Verifica que, al confirmar una venta, la aplicación limpia la mesa y borra
     * los datos viejos para evitar que el usuario compre las mismas cosas por accidente
     * en su siguiente pedido.
     */
    @Test
    fun `Dismiss confirmation clears the cart`() {
        viewModel.addItem(1)

        viewModel.dismissConfirmation()

        val currentState = viewModel.quantities.value
        assertEquals(0, currentState.size)
    }
}
