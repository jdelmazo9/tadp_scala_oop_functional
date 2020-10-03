describe 'GuerreroTests' do

  guerrero = Class.new do
    attr_accessor :fuerza, :vida

    def initialize(vida, fuerza)
      @vida = vida
      @fuerza = fuerza
    end

    invariant { fuerza > 0 && fuerza < 100 }
    invariant { vida > 0 && vida < 100 }

  end

  it 'testPinchaPorVida' do

    un_guerrero = guerrero.new(50,50)

    expect { un_guerrero.vida = 0 }.to raise_error(ConditionError)

  end

  it 'testVidaPositiva' do
    un_guerrero = guerrero.new(50,50)
    un_guerrero.vida = 50

    expect(un_guerrero.vida).to be 50
  end

end


describe 'PilaTests' do

  pila = Class.new do

    attr_accessor :current_node, :capacity

    invariant { capacity >= 0 }

    post { empty? }
    def initialize(capacity)
      @capacity = capacity
      @current_node = nil
    end

    pre { !full? }
    post { height > 0 }
    def push(element)
      @current_node = Node.new(element, current_node)
    end

    pre { !empty? }
    def pop
      element = top
      @current_node = @current_node.next_node
      element
    end

    pre { !empty? }
    def top
      current_node.element
    end

    def height
      empty? ? 0 : current_node.size
    end

    def empty?
      current_node.nil?
    end

    def full?
      height == capacity
    end

    Node = Struct.new(:element, :next_node) do
      def size
        next_node.nil? ? 1 : 1 + next_node.size
      end
    end

  end

  it 'testJugandoConLaPila' do
    unaPila = pila.new(10)
    unaPila.push("AEEEEEEEEEEEA")
    unaPila.push("¡Dale negro!")
    unaPila.push("Que todo el mundo grite")
    unaPila.push("Sabalé")
    unaPila.push("Sabalé")
    unaPila.push("Sabalero")
    unaPila.push("La culpa es de este pueblo")
    unaPila.push("Sabalé")
    unaPila.push("Sabalé")


    expect(unaPila.full?).not_to be true

    puts("....................................")
    puts("Los Palmeras - Soy Sabalero (Chorus)")
    puts("....................................")
    for i in 0..8
      puts(unaPila.pop)
    end
  end

  it 'pilaTieneAlgo' do

    unaPila = pila.new(5)
    unaPila.push(1)

    expect(unaPila.height).to be 1

  end

  it 'testPushNoCumplePrecondicion' do

    unaPila = pila.new(5)

    expect {
      for i in 0..5
        unaPila.push(i)
      end
    }.to raise_error(ConditionError)

  end

  it 'testPopNoCumplePrecondicion' do

    unaPila = pila.new(5)

    expect {
      unaPila.pop
    }.to raise_error(ConditionError)

  end

  it 'testTopNoCumplePrecondicion' do
    unaPila = pila.new(5)

    expect {
      unaPila.top
    }.to raise_error(ConditionError)
  end

  it 'testRompePorInvariant' do
    expect {
      unaPila = pila.new(-1)
    }.to raise_error(ConditionError)

  end


end