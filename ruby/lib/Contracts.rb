# 1) OK - entiendo que estas agregando metodos. Puedo hacer algo cuando lo agregas
# 2) cambiar el metodo que agregas para que se ejecute lo que yo quiero. Ej. agregas mensaje_1 y yo lo piso con otro mensaje_1
# 3) defino un nuevo metodo que haga:
#     * llamo a todos los before
#     * ejecuto lo que vos querias
#     * llamo a todos los after


class Class
  attr_accessor :bloques_after, :bloques_before

  def ejecutar_bloques_before
    # puts self.bloques_before.inspect
    @bloques_before.each { |bloque| bloque.call }
  end

  def ejecutar_bloques_after
    @bloques_after.each { |bloque| bloque.call }
  end

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
          self.class.ejecutar_bloques_before
          self.send("#{method_name}_2".to_sym)
          self.class.ejecutar_bloques_after
        end
      end
    end
  end

end


class MiClase
  before_and_after_each_call(
      # Bloque Before. Se ejecuta antes de cada mensaje
      proc{ puts "aeeeeaa" },
      # Bloque After. Se ejecuta después de cada mensaje
      proc{ puts "sabale sabale" }
  )

  def sabalero_soy
    puts "yo soy sabalero"
    return 0
  end

  # def mensaje_2
  #   puts "mensaje_2"
  #   return 3
  # end
end

miclase = MiClase.new
miclase.sabalero_soy
