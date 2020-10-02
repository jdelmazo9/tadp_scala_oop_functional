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
describe 'RepoTests' do

  guerrero = Class.new do
    attr_accessor :vida

    invariant { vida > 0 && vida < 100 }
  end

  it 'testPinchaPorVida' do

    un_guerrero = guerrero.new

    expect { un_guerrero.vida = 0 }.to raise_error(ConditionError)

  end

  it 'testVidaPositiva' do
    un_guerrero2 = guerrero.new
    un_guerrero2.vida = 100

    expect { un_guerrero2.vida }.equal?50
  end


end