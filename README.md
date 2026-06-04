# DemoCST

DemoCST e um exemplo de agente cognitivo implementado em Java com o framework CST
(Cognitive Systems Toolkit) e o simulador WS3D. O projeto cria uma criatura em um
ambiente 2D com alimentos, organiza sua mente em codelets e usa memorias
compartilhadas para ligar sensores, percepcao, comportamentos e acoes motoras.

O comportamento principal do agente e simples: perceber macas visiveis, escolher a
maca mais proxima, mover-se ate ela e come-la. Quando nenhuma maca conhecida esta
disponivel, o agente executa uma acao de forrageamento.

## Estrutura do projeto

```text
src/main/java/
+-- ExperimentMain.java
+-- Environment.java
+-- AgentMind.java
+-- codelets/
|   +-- sensors/
|   +-- perception/
|   +-- behaviors/
|   +-- motor/
+-- support/
```

### `ExperimentMain`

E o ponto de entrada da aplicacao. Ele:

- configura o nivel de log dos codelets;
- cria o ambiente de simulacao (`Environment`);
- cria a mente do agente (`AgentMind`);
- abre o `MindViewer`, usado para visualizar a organizacao da mente e seus
  codelets comportamentais.

### `Environment`

Inicializa o mundo WS3D:

- reseta o ambiente;
- cria alguns alimentos em posicoes fixas;
- cria uma criatura na posicao inicial;
- inicia a criatura;
- inicia um gerador de recursos para fazer novos itens aparecerem no ambiente.

O WS3D e acessado por meio da biblioteca `WS3DProxy`.

### `AgentMind`

Define a arquitetura cognitiva do agente. A classe estende `Mind` do CST e cria:

- grupos de codelets: `Sensory`, `Motor`, `Perception` e `Behavioral`;
- grupos de memoria: `Sensory`, `Motor` e `Working`;
- objetos e containers de memoria compartilhados;
- codelets sensores, perceptivos, comportamentais e motores.

Ao final da construcao, todos os codelets recebem um passo de tempo de 200 ms e o
ciclo cognitivo e iniciado com `start()`.

## Memorias

As memorias sao o mecanismo de comunicacao entre os codelets:

| Memoria | Tipo | Funcao |
| --- | --- | --- |
| `VISION` | `MemoryObject` | Lista de objetos atualmente no campo visual da criatura. |
| `INNER` | `MemoryObject` | Estado interno da criatura, representado como `Idea`: posicao, direcao, combustivel e campo de visao. |
| `KNOWN_APPLES` | `MemoryObject` | Lista de macas positivas ja detectadas pelo agente. |
| `CLOSEST_APPLE` | `MemoryObject` | Referencia para a maca conhecida mais proxima. |
| `LEGS` | `MemoryContainer` | Comandos motores para movimento. Permite competicao por ativacao entre comportamentos. |
| `HANDS` | `MemoryObject` | Comandos de manipulacao, como comer um objeto. |

## Codelets

### Sensores

- `Vision`: atualiza o estado da criatura e escreve em `VISION` os objetos vistos.
- `InnerSense`: escreve em `INNER` informacoes internas da criatura, como posicao,
  pitch, combustivel e geometria do campo de visao.

### Percepcao

- `AppleDetector`: le `VISION`, filtra objetos cujo nome indica alimento positivo
  (`PFood`, exceto `NPFood`) e adiciona esses objetos em `KNOWN_APPLES`.
- `ClosestAppleDetector`: le `KNOWN_APPLES` e `INNER`, calcula a distancia entre a
  criatura e cada maca conhecida, e grava a mais proxima em `CLOSEST_APPLE`.

### Comportamentos

- `GoToClosestApple`: se existir uma maca mais proxima e ela estiver fora do
  alcance, envia um comando `GOTO` para `LEGS`. Se a maca estiver dentro do
  alcance, envia `GOTO` com velocidade zero.
- `EatClosestApple`: se a maca mais proxima estiver dentro do alcance, envia um
  comando `EATIT` para `HANDS` e remove a maca da lista de conhecidas.
- `Forage`: quando nao ha macas conhecidas, envia um comando `FORAGE` para
  `LEGS`, fazendo a criatura rotacionar para procurar novos objetos.

### Motores

- `LegsActionCodelet`: interpreta comandos em `LEGS`. Para `GOTO`, chama
  `moveto`; para `FORAGE`, chama `rotate`.
- `HandsActionCodelet`: interpreta comandos em `HANDS`. Para `EATIT`, chama
  `eatIt`; tambem possui suporte para `PICKUP` e `BURY`.

## Fluxo cognitivo

1. `Vision` coleta os objetos visiveis.
2. `InnerSense` coleta o estado proprio da criatura.
3. `AppleDetector` identifica alimentos positivos vistos e atualiza
   `KNOWN_APPLES`.
4. `ClosestAppleDetector` seleciona a maca conhecida mais proxima.
5. `GoToClosestApple`, `EatClosestApple` e `Forage` competem ou cooperam por meio
   das memorias motoras.
6. `LegsActionCodelet` e `HandsActionCodelet` enviam os comandos efetivos para a
   criatura no WS3D.

## Dependencias

O projeto usa Gradle e Java 17. As principais dependencias estao em
`build.gradle`:

- `com.github.CST-Group:cst-desktop:1.1.4`
- `com.github.CST-Group:WS3DProxy:0.0.7`
- `org.json:json:20180813`
- `junit:junit:4.10` para testes

## Como executar

Primeiro, inicie o simulador WS3D. O repositorio inclui um script Docker:

```bash
./ws3d.sh
```

Depois, em outro terminal, execute a aplicacao:

```bash
./gradlew run
```

Tambem e possivel gerar um JAR completo:

```bash
./gradlew jar
```

O JAR gerado inclui as dependencias de runtime no arquivo empacotado.

## Observacoes

- O simulador WS3D deve estar rodando antes da aplicacao Java.
- O script `ws3d.sh` usa Docker, `xhost +`, rede do host e modo privilegiado.
- A classe principal configurada no Gradle e `ExperimentMain`.
- O projeto esta licenciado sob Apache License 2.0, conforme o arquivo
  `LICENSE`.
