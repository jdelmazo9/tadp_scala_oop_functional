# 1) OK - entiendo que estas agregando metodos. Puedo hacer algo cuando lo agregas
# 2) cambiar el metodo que agregas para que se ejecute lo que yo quiero. Ej. agregas mensaje_1 y yo lo piso con otro mensaje_1
# 3) defino un nuevo metodo que haga:
#     * llamo a todos los before
#     * ejecuto lo que vos querias
#     * llamo a todos los after
#

class ContractMethod
  attr_accessor :method_name

  def initialize(method, method_name)
    @method = method
    @method_name = method_name
  end

  def exec_on(instance, args, block)
    @method.bind(instance).call(*args,&block)
    # @method.bind(instance).call(*args)
  end
end

class ChinchulinException < StandardError
end

class ConditionError < StandardError
end

class Module
  attr_accessor :after_blocks, :before_blocks

  private def before_and_after_each_call(bloque_before, bloque_after)
    @before_blocks ||= []
    @after_blocks ||= []
    @before_blocks << bloque_before
    @after_blocks << bloque_after
  end

  def proc_with_block_condition(bloque, error_message = "Condition is not verified")
    proc do
      result = instance_eval(&bloque)
      unless result.is_a? TrueClass or result.is_a? FalseClass
        raise ChinchulinException.new "no me hagas la tramposa que soy sabalero como vos, chinchulin"
      end
      unless result
        raise ConditionError.new error_message
      end
    end
  end

  private def invariant(&bloque)
   @after_blocks ||= []
   @after_blocks << proc_with_block_condition(bloque, "No pode dejar de ser sabalero papa. Y si no eras sabalero, que estas esperando?")
  end

  private def pre(&bloque)
    @pre = proc_with_block_condition(bloque, "No cumple la sabalera precondicion")
  end

  private def post(&bloque)
    @post = proc_with_block_condition(bloque, "No cumple la sabalera postcondicion")
  end

  private def method_added(method_name, *args)
    @overwritten_contract_methods = [] if @overwritten_contract_methods.nil?
    if !@overwritten_contract_methods.any? {|contract_method| contract_method.method_name == method_name} && method_name != :method_added
      contractMethod = ContractMethod.new(self.instance_method(method_name), method_name)
      @overwritten_contract_methods << contractMethod
      self.send(:redefine_method, contractMethod,  @pre, @post)
      @pre = nil
      @post = nil
    end
  end

  private def redefine_method(contractMethod, pre, post)
    self.define_method(contractMethod.method_name) do |*args, &block|
      @external_level = true if @external_level.nil?
      local_external_level = @external_level
      @external_level = false
      if local_external_level
        instance_eval(&pre) unless pre.nil?
        self.class.before_blocks&.each do |proc|
          instance_eval(&proc)
        end
      end
      ret = contractMethod.exec_on(self, args, block)
      if local_external_level
        self.class.after_blocks&.each do |proc|
          instance_eval(&proc)
        end
        instance_eval(&post) unless post.nil?
      end
      @external_level = local_external_level
      ret
    end
  end
end

#
# class MiClase
#
#   attr_accessor :la_variable_sabalera
#
#   def initialize
#     @la_variable_sabalera = 'aaaeeeaaa (la variable sabalera)'
#     @la_variable_no_tan_sabalera = 'aea'
#   end
#
#   before_and_after_each_call(
#       # Bloque Before. Se ejecuta antes de cada mensaje
#       proc { puts la_variable_sabalera },
#       # Bloque After. Se ejecuta después de cada mensaje
#       proc{ puts "sabale sabale #{la_variable_sabalera}"}
#   )
#
#   before_and_after_each_call(
#       # Bloque Before. Se ejecuta antes de cada mensaje
#       proc { puts @la_variable_no_tan_sabalera },
#       # Bloque After. Se ejecuta después de cada mensaje
#       proc{ puts "sabale sabale #{@la_variable_no_tan_sabalera}"}
#   )
#
#
#   def sabalero_soy_1_arg(arg1)
#     puts "soy el metodo que tiene un argumento #{arg1}"
#     return 0
#   end
#
#   def sabalero_soy_2_arg(arg1, arg2)
#     puts "tengo 2 argumentos #{arg1} #{arg2}"
#     return 0
#   end
#
#   def sabalero_soy_0_arg
#     puts "yo no tengo argumentos pero soy sabalero"
#     return 0
#   end
# end
#
# miclase = MiClase.new
# miclase.sabalero_soy_0_arg
# miclase.sabalero_soy_1_arg(1)
# miclase.sabalero_soy_2_arg(1, 2)


class Sabalero

  attr_accessor :nombre, :vino_en_sangre, :amor_por_el_pulga, :cantidad_de_sabalamigos

    before_and_after_each_call(
        # Bloque Before. Se ejecuta antes de cada mensaje
        proc {  },
        # Bloque After. Se ejecuta después de cada mensaje
        proc {  }
    )

  invariant { amor_por_el_pulga >= 100 }
  invariant { vino_en_sangre > 10 && vino_en_sangre < 1500 }

  def initialize(nombre, amor, vino)
    @nombre = nombre
    @vino_en_sangre = vino
    @amor_por_el_pulga = amor
    @cantidad_de_sabalamigos = 0
  end

  post {1 == 1}
  def otraCosa
    puts "yo hago otra cosa"
  end

  post { @vino_en_sangre < 1400 }
  pre { amor_por_el_pulga > 0 }
  def convidar_de_la_jarra(otro)
    otro.vino_en_sangre += amor_por_el_pulga
    self.vino_en_sangre = 1300
    @cantidad_de_sabalamigos += 1
  end
end


saba = Sabalero.new("Saba",150, 20)
# lero = Sabalero.new("Lero", 1300, 1300)

saba.vino_en_sangre = 1000
# saba.convidar_de_la_jarra(lero)

# puts(saba.vino_en_sangre)
# puts lero.vino_en_sangre