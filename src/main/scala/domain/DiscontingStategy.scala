package domain

trait DiscontingStategy {
  def run(cart: ShoppingCart): Double
}

