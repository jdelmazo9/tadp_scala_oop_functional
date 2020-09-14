# 1) OK - entiendo que estas agregando metodos. Puedo hacer algo cuando lo agregas
# 2) cambiar el metodo que agregas para que se ejecute lo que yo quiero. Ej. agregas mensaje_1 y yo lo piso con otro mensaje_1
# 3) defino un nuevo metodo que haga:
#     * llamo a todos los before
#     * ejecuto lo que vos querias
#     * llamo a todos los after


class Class
  attr_accessor :bloques_after, :bloques_before

  def initialize

  end

  def ejecutar_bloques_before
    puts self
    puts "ejecutar bloques before"
    self.bloques_before.each { |bloque| bloque.call }
    # self.singleton_class.bloques_before.each { |bloque| bloque.call }
  end

  private def foo
    puts "foo"
  end

  private def before_and_after_each_call(bloque_before, bloque_after)
    if self.singleton_class.bloques_before.nil?
      self.singleton_class.bloques_before = []
    end
    if self.singleton_class.bloques_after.nil?
      self.singleton_class.bloques_after = []
    end
    self.singleton_class.bloques_before.append bloque_before
    self.singleton_class.bloques_after.append bloque_after
  end

  private def method_added(method_name)
    if @ya_sobrescribi_estos.nil?
      @ya_sobrescribi_estos = [:method_added, :before_and_after_each_call]
    end
    super
    puts "agregando metodo #{method_name}"
    self.class_eval do
      unless @ya_sobrescribi_estos.include?(method_name)
        puts "modificando metodo #{method_name}"
        @ya_sobrescribi_estos << method_name
        @ya_sobrescribi_estos << "#{method_name}_2".to_sym
        alias_method "#{method_name}_2".to_sym, method_name
        self.define_method(method_name) do |a:nil|
          # send(:ejecutar_bloques_before)
          # ejecutar_bloques_before
          # puts "aaa"
          puts self
          self.singleton_class.ejecutar_bloques_before
          self.send("#{method_name}_2".to_sym)
          # @bloques_after.each { |&bloque| bloque.call }
        end
      end
    end
  end

  # private def define_method(method_name, block)
  #   puts("hola")
  #   nuevo = proc{ puts "hola"; block; puts "chau"}
  #   super(method_name, nuevo)
  # end

end


class MiClase
  before_and_after_each_call(
      # Bloque Before. Se ejecuta antes de cada mensaje
      proc{ puts “Entré a un mensaje” },
      # Bloque After. Se ejecuta después de cada mensaje
      proc{ puts “Salí de un mensaje” }
  )

  def mensaje_0
    puts "yo soy sabalero"
    return 0
  end

  def mensaje_1
    puts "mensaje_1"
    return 5
  end

  # def mensaje_falopa
  #   self.ejecutar_bloques_before
  # end

  #
  # def mensaje_2
  #   puts "mensaje_2"
  #   return 3
  # end
  #
  # def mensaje_1_2
  #   puts "aaaaaaea sabale sabale"
  # end
end

# puts MiClase.class
# puts MiClase.ancestors
# puts MiClase.singleton_class.ancestors
# MiClase.new.mensaje_1
#
#
# puts MiClase.class
# miclase = MiClase.new
# puts miclase
# # miclase.mensaje_0
# miclase.mensaje_1
# # MiClase.new.mensaje_1

# puts MiClase.new
# puts MiClase.new.singleton_class
# # MiClase.new.mensaje_1
# # MiClase.new.mensaje_1