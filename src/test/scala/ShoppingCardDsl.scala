import cats.implicits.toTraverseOps
import domain.{Product, ShoppingCart, SimpleTaxationStrategy}
import interpreter.InMemoryShoppingCartInterpreter.{ShoppingCardDslInterpreter, ShoppingCartRepoState}
import org.scalatestplus.play.PlaySpec

class ShoppingCardDsl extends PlaySpec {

  "ShoppingCardDsl" should {
    "create a shopping cart given an id" in {
      val state: ShoppingCartRepoState[Unit] = ShoppingCardDslInterpreter.create("id")
      val result = {
      for {
        initial <- state
        newState <- ShoppingCardDslInterpreter.find("id")
        finalShoppingCart <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(oldCart, Product("Dove Soap", 11)))
      } yield {
        finalShoppingCart
      }
      }
      val finalShoppingCart = result.runS(Map.empty)
      finalShoppingCart.value.get("id") mustBe(Some(ShoppingCart("id", List(Product("Dove Soap", 11)))))

    }

    "calculate total of items inside shopping cart" in {

    val state: ShoppingCartRepoState[Unit] = ShoppingCardDslInterpreter.create("id")
    val result = {
      for {
        initial <- state
        newState <- ShoppingCardDslInterpreter.find("id")
        cart1 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(oldCart, Product("Dove Soap", 39.99)))
        cart2 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart1.get, Product("Dove Soap", 39.99)))
        cart3 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart2.get, Product("Dove Soap", 39.99)))
        cart4 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart3.get, Product("Dove Soap", 39.99)))
        cart5 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart4.get, Product("Dove Soap", 39.99)))
      } yield {
        ShoppingCardDslInterpreter.find("id")
      }
    }
    val finalShoppingCart: Map[String, ShoppingCart] = result.runS(Map.empty).value

      finalShoppingCart("id").cartTotal.setScale(2, BigDecimal.RoundingMode.HALF_UP) mustBe(199.95)
  }



    "calculate total of items inside cart after taxation" in {
      val state: ShoppingCartRepoState[Unit] = ShoppingCardDslInterpreter.create("id")
      val result = {
        for {
          initial <- state
          newState <- ShoppingCardDslInterpreter.find("id")
          cart1 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(oldCart, Product("Dove Soap", 39.99)))
          cart2 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart1.get, Product("Dove Soap", 39.99)))
          cart3 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart2.get, Product("Dove Soap", 99.99)))
          cart4 <- newState.traverse(oldCart=> ShoppingCardDslInterpreter.addItems(cart3.get, Product("Dove Soap", 99.99)))
        } yield {
          cart4
        }
      }

      val finalShoppingCart: Map[String, ShoppingCart] = result.runS(Map.empty).value
      ShoppingCardDslInterpreter.calculateTotal(finalShoppingCart("id"), SimpleTaxationStrategy(12.5))
        .setScale(2, BigDecimal.RoundingMode.HALF_UP) mustBe (314.96)

    }


  }

}
