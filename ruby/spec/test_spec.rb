# describe Prueba do
#   let(:prueba) { Prueba.new }
#
#   describe '#materia' do
#     it 'debería pasar este test' do
#       expect(prueba.materia).to be :tadp
#     end
#   end
# end
#
# describe 'GuerreroTests' do
#
#   guerrero = Class.new do
#     attr_accessor :fuerza, :vida
#
#     def initialize(vida, fuerza)
#       @vida = vida
#       @fuerza = fuerza
#     end
#
#     invariant { fuerza > 0 && fuerza < 100 }
#     invariant { vida > 0 && vida < 100 }
#
#   end
#
#   it 'testPinchaPorVida' do
#
#     un_guerrero = guerrero.new(50,50)
#
#     expect { un_guerrero.vida = 0 }.to raise_error(ConditionError)
#
#   end
#
#   it 'testVidaPositiva' do
#     un_guerrero = guerrero.new(50,50)
#     un_guerrero.vida = 50
#
#     expect(un_guerrero.vida).to be 50
#   end
#
# end

describe 'operacionesTest' do

  operaciones = Class.new do
    #precondición de dividir
    pre { divisor != 0 }
    #postcondición de dividir
    post { |result| result * divisor == dividendo }
    def dividir(dividendo, divisor)
      dividendo / divisor
    end

    # este método no se ve afectado por ninguna pre/post condición
    def restar(minuendo, sustraendo)
      minuendo - sustraendo
    end
  end

  # > Operaciones.new.dividir(4, 2)
  # => 2
  it 'se puede dividir por 2' do
    expect(operaciones.new().dividir(4, 2)).to be 2
  end

  # > Operaciones.new.dividir(4, 0)
  # RuntimeError: Failed to meet preconditions
  it 'no se puede dividir por 0' do
    expect{operaciones.new().dividir(4, 0)}.to raise_error(ConditionError)
  end


end