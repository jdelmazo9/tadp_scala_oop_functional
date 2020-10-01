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
describe '' do
  it '' do
    guerrero = Class.new do
      attr_accessor :vida

      invariant { vida > 0 }
    end

    un_guerrero = guerrero.new
    un_guerrero.vida=10

    expect { un_guerrero.vida = 0 }.to raise_error(ConditionError)
  end
end