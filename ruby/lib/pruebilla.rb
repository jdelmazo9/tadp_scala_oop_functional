
class Sarasa
  def metodoCualquiera instancia
    a = 0
    eval( lambda {"a = 11"}.call )
    puts a
    bloque = proc { puts a + b }
    instancia.instance_exec &bloque
  end
end

class ClaseCualquieraQueNoConoceAA
  attr_accessor :b


  def initialize
    @b = 1
  end
end

Sarasa.new.metodoCualquiera ClaseCualquieraQueNoConoceAA.new