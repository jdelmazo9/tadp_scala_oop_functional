# 1) OK - entiendo que estas agregando metodos. Puedo hacer algo cuando lo agregas
# 2) cambiar el metodo que agregas para que se ejecute lo que yo quiero. Ej. agregas mensaje_1 y yo lo piso con otro mensaje_1
# 3) defino un nuevo metodo que haga:
#     * llamo a todos los before
#     * ejecuto lo que vos querias
#     * llamo a todos los after

class ContractMethod
  attr_accessor :method_name

  def initialize(method, method_name)
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


  private def method_added(method_name, *args)
    if @overwritten_contract_methods.nil?
      @overwritten_contract_methods = []
    end
    super
    puts "agregando metodo #{method_name}"
    self.class_eval do

      if !@overwritten_contract_methods.any? {|contract_method| contract_method.method_name == method_name} && method_name != :method_added && method_name != :initialize
        puts "modificando metodo #{method_name}"
        contractMethod = ContractMethod.new(self.instance_method(method_name), method_name)
        @overwritten_contract_methods << contractMethod
        self.define_method(method_name) do |*args|
          #puts "SOY EL DEEP Y ESTOY EN: " + @deep.inspect
          if @deep.nil?
            @deep = 0
          end
          deep_local = @deep
          @deep += 1
          if deep_local == 0
            self.class.bloques_before.each do |proc|
              instance_eval(&proc)
            end
          end
          contractMethod.exec_on(self, args)

          if deep_local == 0
            self.class.bloques_after.each do |proc|
              instance_eval(&proc)
            end
          end
          @deep -= 1
        end
      end
    end
  end
end


class MiClase

  attr_accessor :la_variable_sabalera

  def initialize
    @la_variable_sabalera = 'aaaeeeaaa (la variable sabalera)'
    @la_variable_no_tan_sabalera = 'aea'
  end

  before_and_after_each_call(
      # Bloque Before. Se ejecuta antes de cada mensaje
      proc { puts sabalero_soy_0_arg },
      # Bloque After. Se ejecuta después de cada mensaje
      proc{ puts "sabale sabale #{la_variable_sabalera}" }
  )


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
miclase.sabalero_soy_0_arg
miclase.sabalero_soy_1_arg(1)
miclase.sabalero_soy_2_arg(1, 2)

