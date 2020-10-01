
# 1) OK - entiendo que estas agregando metodos. Puedo hacer algo cuando lo agregas
# 2) cambiar el metodo que agregas para que se ejecute lo que yo quiero. Ej. agregas mensaje_1 y yo lo piso con otro mensaje_1
# 3) defino un nuevo metodo que haga:
#     * llamo a todos los before
#     * ejecuto lo que vos querias
#     * llamo a todos los after
#

class ContractMethod
  attr_accessor :method_name

  def initialize(method_name, method)
    @method = method
    @method_name = method_name
  end

  def exec_on(instance, args)
    @method.bind(instance).call(*args)
  end
end

class Class
  attr_accessor :bloques_after, :bloques_before

  private def before_and_after_each_call(bloque_before, bloque_after)
    if @bloques_before.nil?
      @bloques_before = []
    end
    if @bloques_after.nil?
      @bloques_after = []
    end
    @bloques_before << bloque_before
    @bloques_after << bloque_after
  end

  private def invariant(&bloque)
    if @bloques_after.nil?
      @bloques_after = []
    end
    @bloques_after << proc do
      unless instance_eval(&bloque)
        raise "No pode dejar de ser sabalero papa. Y si no eras sabalero, que estas esperando?"
      end
    end
  end

  private def method_missing(method_name, *args)
    puts "missing #{method_name}"
    contract_method = @contract_methods.find {|contract_method| contract_method.method_name == method_name}
    puts contract_method.class
    if contract_method.nil?
      super
    else
      self.instance_eval do
        puts self
        execute_before
        ret = contract_method.exec_on(self, args)
        execute_after
        ret
      end
    end
  end

  def execute_before
    unless self.class.bloques_before.nil?
      self.class.bloques_before.each do |proc|
        instance_eval(&proc)
      end
    end
  end

  def execute_after
    unless self.class.bloques_after.nil?
      self.class.bloques_after.each do |proc|
        instance_eval(&proc)
      end
    end
  end

  private def method_added(method_name, *args)
    if [:method_added, :method_missing].include? method_name
      return
    end
    if @contract_methods.nil?
      @contract_methods = []
    end
    puts "agregando metodo #{method_name}"

    contractMethod = ContractMethod.new(method_name, self.instance_method(method_name))
    @contract_methods << contractMethod
    self.remove_method method_name

    # if !@contract_methods.any? {|contract_method| contract_method.method_name == method_name} && method_name != :method_added && method_name != :initialize
    #   puts "modificando metodo #{method_name}"
    #   contractMethod = ContractMethod.new(self.instance_method(method_name), method_name)
    #   @contract_methods << contractMethod
    #   self.define_method(method_name) do |*args|
    #     # puts "SOY EL DEEP Y ESTOY EN: " + @deep.inspect
    #     if @deep.nil?
    #       @deep = 0
    #     end
    #     deep_local = @deep
    #     @deep += 1
    #     if deep_local == 0 and not self.class.bloques_before.nil?
    #       self.class.bloques_before.each do |proc|
    #         instance_eval(&proc)
    #       end
    #     end
    #     ret = contractMethod.exec_on(self, args)
    #
    #     if deep_local == 0 and not self.class.bloques_after.nil?
    #       self.class.bloques_after.each do |proc|
    #         instance_eval(&proc)
    #       end
    #     end
    #     @deep -= 1
    #     # puts "ESTOY SALIENDO CON DEEP: " + @deep.inspect
    #     ret
    #   end
    # end
  end
end


class MiClase

  private def method_missing(symbol, *args)
    self.class.send(:method_missing, symbol, args)
  end

  attr_accessor :la_variable_sabalera

  def initialize
    @la_variable_sabalera = 'aaaeeeaaa (la variable sabalera)'
    @la_variable_no_tan_sabalera = 'aea'
  end

  before_and_after_each_call(
      # Bloque Before. Se ejecuta antes de cada mensaje
      proc { self.sabalero_soy_0_arg },
      # Bloque After. Se ejecuta después de cada mensaje
      proc{ puts "sabale sabale #{la_variable_sabalera}"; return 1 }
  )

  # before_and_after_each_call(
  #     # Bloque Before. Se ejecuta antes de cada mensaje
  #     proc { puts @la_variable_no_tan_sabalera },
  #     # Bloque After. Se ejecuta después de cada mensaje
  #     proc{ puts "sabale sabale #{@la_variable_no_tan_sabalera}"}
  # )


  def sabalero_soy_1_arg(arg1)
    puts "soy el metodo que tiene un argumento #{arg1}"
    return 0
  end

  def sabalero_soy_2_arg(arg1, arg2)
    puts "tengo 2 argumentos #{arg1} #{arg2}"
    return 0
  end

  def sabalero_soy_0_arg
    puts "yo no tengo argumentos pero soy sabalero"
    return 0
  end
end

# MiClase.

miclase = MiClase.new
puts MiClase.methods false
# miclase.sabalero_soy_0_arg
miclase.sabalero_soy_1_arg(1)
# miclase.sabalero_soy_2_arg(1, 2)


# class Sabalero
#
#   attr_accessor :vino_en_sangre, :amor_por_el_pulga
#
#   invariant { amor_por_el_pulga >= 100 }
#   invariant { vino_en_sangre > 10 && vino_en_sangre < 1500 }
#
#   def initialize(amor, vino)
#     @vino_en_sangre = vino
#     @amor_por_el_pulga = amor
#   end
#
#   def convidar_de_la_jarra(otro)
#     otro.vino_en_sangre += amor_por_el_pulga
#   end
#
# end
#
# saba = Sabalero.new(150, 20)
# lero = Sabalero.new(1500, 1500)
#
# saba.convidar_de_la_jarra(lero)