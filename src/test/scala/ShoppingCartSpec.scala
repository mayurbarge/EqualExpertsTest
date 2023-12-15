import domain.ShoppingCart
import org.scalatestplus.play.PlaySpec

class ShoppingCartSpec extends PlaySpec {
  "A shopping cart" should {
    "be empty initially" in {
      val shoppingCart = ShoppingCart("1", List.empty)
      shoppingCart.items mustBe List.empty[Double]
    }
  }

}
