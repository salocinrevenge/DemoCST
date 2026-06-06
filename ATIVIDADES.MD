# Atividade 2
O profiling do funcionamento de um codelet é uma maneira de conseguirmos obter informações sobre o funcionamento dos codelets durante o funcionamento de uma arquitetura cognitiva. Existem 2 modos de fazer o profiling de um Codelet. O primeiro deles visa somente rastrear o tempo de execução de um Codelet. Para isso, podemos usar as funções:

public synchronized boolean isProfiling()
public synchronized void setProfiling(boolean isProfiling)
da classe Codelet, para gerar um arquivo de texto com os tempos de execução de cada vez que a classe proc() é executada. Com isso conseguimos descobrir o tempo que está sendo utilizado pelo Codelet, e definir um TimeStep adequado para o funcionamento do Codelet. 

Utilize o método Codelet.setProfiling(true) para efetuar o profile de algum codelet e verifique o tempo de execução do codelet.

O segundo método de executar o profiling de um codelet é armazenar em arquivo texto tanto os dados de entrada com os dados de saída do Codelet, a cada vez que a função proc() é executada. Para isso, podemos usar a função:

public void setCodeletProfiler(String filePath, String fileName, String mindIdentifier,Integer queueSize, Long intervalTimeMillis, FileFormat fileFormat)
para definir um arquivo texto onde esses dados são armazenados, para posterior análise. 

Utilize o método Codelet.setCodeletProfiler() para fazer o rastreamento do funcionamento de um dos Codelets e observar como esses dados podem ser armazenados e posteriormente utilizados para avaliar o funcionamento adequado do Codelet. 

# Atividade 3
Nas versões mais modernas do CST, existem dois modos distintos de operar Codelets. O modo Timer-based é o default, que cria ciclos com TimeSteps definidos para chamar a execução da função proc() que realiza a atividade do Codelet (o default é um TimeStep de 300ms). Entretanto, em muitos casos, isso acaba levando a desperdício de tempo de execução, pois não há mudanças nos MemoryObjects da entrada dos Codelets, e a função acaba sendo chamada com os mesmos valores anteriores muitas vezes. Para evitar isso, criou-se a opção de transformar o modo de operação de um Codelet para publish-subscribe. Nesse modo de operação, a função proc() de um Codelet só é chamada quando houver alguma modificação em algum dos Memories de entrada do Codelet. Para converter um Codelet para o modo publish-subscribe, basta utilizar a função:

public synchronized void setPublishSubscribe(boolean enable)
Analise os Codelets da aplicação DemoCST e defina quais os Codelets que podem ser convertidos para o modo publish-subscribe e faça essa modificação no código do programa. Em princípio, todos os Codelets que não são Codelets sensoriais podem ser convertidos em modo Publish-subscribe. Analise a diferença de sua operação, utilizando a função de setProfiling e observando os arquivos de profile, em comparação com os obtidos pelo modo de operação Timer-based. 