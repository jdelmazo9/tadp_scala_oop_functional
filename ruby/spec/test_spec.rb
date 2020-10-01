
  #  let(:prueba) { Prueba.new }

  #describe '#materia' do
  # it 'debería pasar este test' do
  #   expect(prueba.materia).to be :tadp
  # end
  #end

  describe 'class contract' do
    it 'lanzar_exception_si_la_vida_es_negativa' do
      guerrero = Class.new do
          attr_accessor :vida, :fuerza

          invariant { vida >= 0 }
          #     invariant { fuerza > 0 && fuerza < 100 }

          def atacar(otro)
            otro.vida -= fuerza
          end
      end
      un_guerrero = guerrero.new
      expect { un_guerrero.vida = -1 }.to raise_exception
      #expect { raise }.to raise_exception(ConditionError)


    end

  end
