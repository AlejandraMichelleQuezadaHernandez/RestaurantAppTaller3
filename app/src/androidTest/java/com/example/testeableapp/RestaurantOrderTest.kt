package com.example.testeableapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.testeableapp.model.MenuData
import org.junit.Rule
import org.junit.Test

class RestaurantOrderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test:Mensaje de pedido vacío visible al
     * inicio
     */
    @Test
    fun Show_message_of_order_empty(){
        composeTestRule.setContent {
            RestaurantOrderApp(viewModel = RestaurantViewModel())
        }
        composeTestRule
            .onNodeWithTag("emptyOrderMessage")
            .assertExists()
            .assertTextEquals("El pedido está vacío. Añade productos del menú.")
    }

    /**
     * Test:Todos los items del menú visibles
     */
    @Test
    fun Show_menu_items_visible(){
        composeTestRule.setContent {
            RestaurantOrderApp(viewModel = RestaurantViewModel())
        }
        composeTestRule
            .onNodeWithText("Patatas Bravas")
            .assertExists()
        composeTestRule
            .onNodeWithText("Calamares")
            .assertExists()
        composeTestRule
            .onNodeWithText("Helado")
            .assertExists()
        composeTestRule
            .onNodeWithText("Croquetas (6 uds)")
            .assertExists()
        composeTestRule
            .onNodeWithText("Tortilla Española")
            .assertExists()
        composeTestRule
            .onNodeWithText("Agua mineral")
            .assertExists()
        composeTestRule
            .onNodeWithText("Refresco")
            .assertExists()
        composeTestRule
            .onNodeWithText("Cerveza")
            .assertExists()
        composeTestRule
            .onNodeWithText("Flan de huevo")
            .assertExists()
        composeTestRule
            .onNodeWithText("Tarta de queso")
            .assertExists()

    }

    /**
     * Test: El total general se actualiza.
     */

    @Test
    fun Update_total_of_items_bought(){
        composeTestRule.setContent {
            RestaurantOrderApp(viewModel = RestaurantViewModel())
        }

        composeTestRule
            .onNodeWithTag("addButton_1")
            .performClick()

        composeTestRule
            .onNodeWithTag("totalValue")
            .performScrollTo()
            .assertIsDisplayed()
            .assertTextEquals("5.50 €")

    }
    /**
     * Test: Se muestra el mensaje de confirmación del pedido,
     * este es necesario ya que la persona necesita un comprobante
     * de lo que ha comprado si en dado caso quiere hacer un reclamo
     * sobre el pago.
     */
    @Test
    fun Show_message_of_order_confirmation(){
        composeTestRule.setContent {
            RestaurantOrderApp(viewModel = RestaurantViewModel())
        }

        composeTestRule
            .onNodeWithTag("addButton_1")
            .performClick()

        composeTestRule
            .onNodeWithTag("placeOrderButton")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithTag("confirmationMessage")
            .assertExists()
            .assertTextEquals("¡Pedido de 1 artículos por un total de 5.50 € recibido! Preparen los fogones.")
    }


    /**
     * Test: Al momento de presionar el boton de aceptar se debe borrar
     * el carrito siendo esto necesario ya que así se limpian los datos
     * y el cliente no se le cobra doble el mismo producto.
     */
    @Test
    fun Confirms_and_clears_the_cart() {
        composeTestRule.setContent {
            RestaurantOrderApp(viewModel = RestaurantViewModel())
        }

        composeTestRule
            .onNodeWithTag("addButton_1")
            .performClick()

        composeTestRule
            .onNodeWithTag("placeOrderButton")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithTag("confirmationOkButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("emptyOrderMessage")
            .assertIsDisplayed()
    }

}

