
class Abc

  def def_geter(method, valor)
    self.class.define_method(method) {valor}
  end

  def exec_proc(&bloque)
    instance_exec &bloque
  end

end

aux = Abc.new
aux.def_geter(:divisor, 0)
aux.method(:divisor)
aux.exec_proc {puts divisor}