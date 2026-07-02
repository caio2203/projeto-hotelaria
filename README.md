# Sistema de Reservas em Hotel — Projeto de Linguagem de Programação II

Aplicação **desktop em JavaFX** para gerenciar as reservas de um hotel: cadastro
de quartos e hóspedes, criação de reservas, check-in/check-out, cancelamento,
fila de espera e histórico de estadias. Os dados são gravados em arquivo ao
fechar e recuperados ao abrir — **sem banco de dados**.

> Este é o projeto da disciplina **Linguagem de Programação II** (Tema 3 –
> Reservas em Hotel). Ele reaproveita o projeto feito em Estruturas de Dados,
> mas adaptado para os requisitos do LP2. O projeto original de ED está em
> **outra branch**.

---

## Índice

1. [O que o professor pediu (e onde está cada coisa)](#1-o-que-o-professor-pediu)
2. [Pré-requisitos](#2-pré-requisitos)
3. [Como rodar (3 jeitos)](#3-como-rodar)
4. [Estrutura de pastas](#4-estrutura-de-pastas)
5. [Como o JavaFX funciona aqui (passo a passo)](#5-como-o-javafx-funciona-aqui)
6. [Como a aplicação funciona (o domínio)](#6-como-a-aplicação-funciona)
7. [Como os dados são salvos (persistência)](#7-como-os-dados-são-salvos)
8. [Coleções genéricas usadas](#8-coleções-genéricas-usadas)
9. [Problemas comuns do JavaFX](#9-problemas-comuns-do-javafx)
10. [Notas e ressalvas](#10-notas-e-ressalvas)

---

## 1. O que o professor pediu

O enunciado exige, **obrigatoriamente**, três recursos do Java. Onde cada um
está no código:

| Requisito | Onde está |
|---|---|
| **a) Coleções genéricas** (`java.util`) | `ArrayList`, `Stack` e `Queue` no pacote `model` (ver seção 8) |
| **b) Manipulação de arquivos** (salvar ao fechar, ler ao abrir) | `model/Persistencia.java` (serialização de objetos) |
| **c) Interface gráfica em JavaFX** | `HotelApp.java` |

Os arquivos de configuração do ambiente de execução (item exigido na entrega
final) são o **`pom.xml`** do Maven, que já declara o JavaFX.

---

## 2. Pré-requisitos

Você precisa de **dois programas** instalados:

1. **JDK 21 ou superior** (não basta o JRE — precisa do kit de desenvolvimento,
   que tem o compilador `javac`).
   - Conferir: abra o terminal e rode `java -version` e `javac -version`.
2. **Apache Maven** (gerencia as bibliotecas e roda o projeto).
   - Conferir: `mvn -version`.

> **Importante:** você **não** precisa baixar o JavaFX na mão. O Maven baixa
> tudo sozinho na primeira execução (ver `pom.xml`). É exatamente por isso que
> usamos Maven — para não ter que configurar o JavaFX manualmente.

### Instalando, se faltar algum

- **Linux (Fedora):** `sudo dnf install java-21-openjdk-devel maven`
- **Linux (Ubuntu/Debian):** `sudo apt install openjdk-21-jdk maven`
- **Windows / macOS:** instale um JDK (ex.: Temurin/Adoptium ou o do site da
  Oracle) e o Maven (https://maven.apache.org/download.cgi), ou simplesmente use
  o IntelliJ, que já vem com tudo (ver opção C abaixo).

---

## 3. Como rodar

Escolha **um** dos três jeitos. O **A** é o mais simples.

### Opção A — Linha de comando com Maven (recomendado)

```bash
git clone https://github.com/caio2203/projeto-ed-hotelaria.git
cd projeto-ed-hotelaria/hotel
mvn javafx:run
```

Na primeira vez o Maven baixa o JavaFX (demora um pouco). Depois a janela abre.

### Opção B — IntelliJ IDEA / Eclipse / VS Code (projeto Maven)

1. Abra a IDE e escolha **abrir/importar projeto**.
2. Aponte para a pasta **`hotel`** (a que tem o `pom.xml`), não a raiz do repo.
3. Espere a IDE baixar as dependências (ela reconhece o Maven sozinha).
4. Rode o goal Maven **`javafx:run`** (aba Maven da IDE) **ou** dê _Run_ na
   classe **`HotelApp`**.

> Se a IDE reclamar de "JavaFX runtime components are missing" ao dar _Run_
> direto na classe, use o goal `javafx:run` — o plugin do Maven resolve o
> classpath do JavaFX automaticamente. Ver seção 9.

### Opção C — Sem Maven (JavaFX SDK na mão)

Só faça assim se **não puder** usar Maven. Baixe o JavaFX SDK em
https://gluonhq.com/products/javafx/, descompacte, e na pasta `hotel`:

```bash
# compilar
javac --module-path /caminho/javafx-sdk/lib --add-modules javafx.controls \
      -d out src/model/*.java src/HotelApp.java

# executar
java  --module-path /caminho/javafx-sdk/lib --add-modules javafx.controls \
      -cp out HotelApp
```

Troque `/caminho/javafx-sdk/lib` pela pasta `lib` do SDK que você baixou.

---

## 4. Estrutura de pastas

```
projeto-hotelaria/
├── README.md                  ← este arquivo
├── instrucoes-UNI3-lp2.md     ← enunciado do professor
└── hotel/                     ← o projeto Maven (abra ESTA pasta na IDE)
    ├── pom.xml                ← configuração do Maven + dependência do JavaFX
    └── src/
        ├── HotelApp.java      ← a tela (JavaFX). É por aqui que o app sobe.
        └── model/             ← as classes do domínio (a "regra de negócio")
            ├── SistemaCentral.java   ← guarda tudo (Singleton)
            ├── Persistencia.java     ← salva/carrega o arquivo
            ├── Hotel.java            ← quartos, reservas e fila de espera
            ├── Quarto.java
            ├── Reserva.java
            ├── Usuario.java          ← classe-mãe (id, nome, login, senha)
            ├── Hospede.java          ← extends Usuario (+ histórico em pilha)
            ├── Administrador.java    ← extends Usuario (+ nível de acesso)
            ├── NivelAcesso.java      ← enum: GERENTE, RECEPCIONISTA
            ├── TipoQuarto.java       ← enum: SOLTEIRO, CASAL, SUITE
            ├── StatusQuarto.java     ← enum: DISPONIVEL, OCUPADO, MANUTENCAO
            └── StatusReserva.java    ← enum: PENDENTE, EM_ANDAMENTO, CONCLUIDA, CANCELADA
```

A pasta `target/` (criada pelo Maven ao compilar) e o arquivo `hotel-dados.ser`
(gerado em tempo de execução) **não** vão para o Git — estão no `.gitignore`.

---

## 5. Como o JavaFX funciona aqui

Esta seção é pra quem nunca mexeu com JavaFX. Tudo abaixo está no arquivo
**`HotelApp.java`**.

### A ideia geral

JavaFX monta a tela como uma **árvore de componentes** (botões, tabelas, campos)
e a aplicação fica "parada" esperando o usuário clicar. Quando o usuário clica,
roda o código que a gente registrou para aquele botão. Os 3 conceitos básicos:

- **`Stage`** = a janela do sistema operacional.
- **`Scene`** = o conteúdo dentro da janela (a árvore de componentes).
- **Componentes** (`Button`, `TableView`, `TextField`...) = o que aparece na cena.

### O ciclo de vida

A classe `HotelApp` herda de `Application`. O JavaFX chama dois métodos sozinho:

```java
public class HotelApp extends Application {

    @Override
    public void start(Stage stage) {   // chamado ao ABRIR a janela
        // ...monta a tela e mostra...
    }

    @Override
    public void stop() {               // chamado ao FECHAR a janela
        Persistencia.salvar(sistema);  // aproveitamos pra salvar os dados
    }

    public static void main(String[] args) {
        launch(args);                  // dá a partida no JavaFX
    }
}
```

- O `main` só chama `launch()`, que inicializa o JavaFX e dispara o `start()`.
- No `start()` a gente carrega os dados salvos, monta as abas e mostra a janela.
- No `stop()` (fechou a janela) a gente grava tudo em arquivo. Por isso os dados
  persistem entre execuções.

### Montando a janela (resumo do `start`)

```java
TabPane abas = new TabPane();                       // um painel de abas
abas.getTabs().addAll(abaQuartos(), abaHospedes(), abaReservas());

Scene scene = new Scene(abas, 820, 560);            // cena de 820x560
stage.setScene(scene);
stage.setTitle("Sistema de Reservas — ...");
stage.show();                                       // mostra a janela
```

Cada aba é montada num método separado (`abaQuartos()`, `abaHospedes()`,
`abaReservas()`) só para o código não virar um bloco gigante.

### Tabelas (`TableView`)

Cada aba tem uma tabela. Para não repetir código, criamos um método auxiliar
`coluna(...)` que recebe o título e uma função que extrai o texto de cada linha:

```java
coluna(tabelaQuartos, "Número", q -> String.valueOf(q.getNumero()));
coluna(tabelaQuartos, "Tipo",   q -> q.getTipo().toString());
```

Para encher a tabela, jogamos a lista do modelo numa `ObservableList`:

```java
tabelaQuartos.setItems(FXCollections.observableArrayList(hotel.getQuartos()));
```

Sempre que algo muda (cadastrou um quarto, fez check-in...), chamamos
`atualizarTudo()`, que recarrega todas as tabelas e combos de uma vez.

### Botões e eventos

Um botão guarda uma ação que roda quando ele é clicado (`setOnAction`):

```java
Button adicionar = new Button("Adicionar quarto");
adicionar.setOnAction(e -> {
    // lê os campos, cria o Quarto, chama hotel.adicionarQuarto(...)
    // e por fim atualizarTudo() pra refletir na tabela
});
```

É esse o padrão da tela inteira: **ler os campos → chamar um método do modelo →
`atualizarTudo()`**. A tela nunca mexe nas regras direto; ela só conversa com as
classes do pacote `model`.

---

## 6. Como a aplicação funciona

O fluxo de uso, aba por aba:

### Aba Quartos
- Cadastrar quarto (número, tipo, capacidade). Todo quarto nasce `DISPONIVEL`.
- **Bloquear** um quarto manda ele para `MANUTENCAO` — ele some das buscas.
- **Liberar** devolve para `DISPONIVEL`.

### Aba Hóspedes
- Cadastrar hóspede (nome, CPF, contato). O login/senha são gerados automáticos
  por enquanto (não temos tela de login ainda).

### Aba Reservas
- Escolher hóspede, quarto e as datas de entrada/saída e clicar **Criar reserva**.
  - Se o quarto estiver **livre** no período: a reserva é criada como `PENDENTE`.
  - Se estiver **ocupado**: o hóspede entra na **fila de espera** (FIFO).
- **Check-in**: a reserva vira `EM_ANDAMENTO` e o quarto fica `OCUPADO`.
- **Check-out**: a reserva é `CONCLUIDA`, o quarto volta a ficar livre, a estadia
  é empilhada no histórico do hóspede, e o **próximo da fila de espera é chamado**.
- **Cancelar**: a reserva vira `CANCELADA` e o quarto é liberado na hora.

A regra de conflito de datas (saber se um quarto está livre) está em
`Reserva.conflitaCom(...)` e `Hotel.buscarQuartosDisponiveis(...)`.

---

## 7. Como os dados são salvos

Atende o requisito **(b)** do enunciado. Está em `model/Persistencia.java`.

- Usamos **serialização de objetos**: como todas as classes do domínio
  implementam `Serializable`, dá para gravar o grafo inteiro (hotéis → quartos,
  reservas, hóspedes, fila e histórico) de uma vez só, e ler de volta do mesmo
  jeito. É bem menos código do que montar/parsear arquivos de texto à mão.
- Ao **fechar** a janela (`stop()`), chamamos `Persistencia.salvar(...)`, que
  grava o arquivo **`hotel-dados.ser`** na pasta onde o programa roda.
- Ao **abrir** (`start()`), chamamos `Persistencia.carregar(...)`. Se o arquivo
  existe, recupera os dados; se é a primeira execução (sem arquivo), populamos
  alguns dados de exemplo para a tela não abrir vazia.

> Quer começar do zero? Apague o arquivo `hotel-dados.ser` e abra o app de novo.

---

## 8. Coleções genéricas usadas

Atende o requisito **(a)**. Tudo é do pacote `java.util` (nenhuma estrutura
feita na mão — essa parte ficou no projeto de Estruturas de Dados, na outra
branch):

| Coleção | Onde | Por quê |
|---|---|---|
| `ArrayList<>` | quartos, reservas e hóspedes do `Hotel` | a gente percorre muito essas listas |
| `Queue<>` (via `LinkedList`) | fila de espera do `Hotel` | FIFO: o primeiro a pedir é o primeiro a ser chamado |
| `Stack<>` | histórico de estadias do `Hospede` | LIFO: a última estadia fica no topo, que é a mais consultada |

---

## 9. Problemas comuns do JavaFX

**"Error: JavaFX runtime components are missing, and are required to run this
application"**
→ Você rodou a classe `HotelApp` direto, sem o JavaFX no module path. Rode com
`mvn javafx:run` (opção A/B) em vez de _Run_ na classe.

**`mvn` não é reconhecido / `command not found`**
→ O Maven não está instalado ou não está no PATH. Veja a seção 2.

**A IDE não acha as classes do JavaFX (`import javafx...` em vermelho)**
→ Você abriu a pasta errada. Abra a pasta **`hotel`** (a que tem o `pom.xml`) e
deixe a IDE importar como projeto Maven.

**`UnsupportedClassVersionError` ou erro de versão do Java**
→ Seu Java é mais antigo que 21. Instale um JDK 21+ (seção 2).

**A primeira execução está demorando muito**
→ Normal: o Maven está baixando o JavaFX. Da segunda vez em diante é rápido.

---

## 10. Notas e ressalvas

### Notas

a) em caso de não recuperação de arquivo, o sistema inicia apenas com o hotel
   vazio — quartos, hóspedes e reservas são cadastrados pelo usuário na
   interface;

b) não uso de arquivos `.fxml`.

### Ressalvas

a) ~~possibilidade de cadastro de dois ou mais quartos com o mesmo número e/ou
   com capacidade igual a 0 (zero)~~ — **corrigido**: o cadastro passou a
   rejeitar número de quarto duplicado e capacidade menor ou igual a zero;

b) ~~não indicação, quando for o caso, de que quarto já se encontra liberado ou
   bloqueado~~ — **corrigido**: ao tentar bloquear/liberar um quarto que já
   está nesse estado, um alerta informa a situação;

c) ~~não confirmação de liberação ou bloqueio de quarto (sem atualização de
   interface)~~ — **corrigido**: bloqueio e liberação exibem alerta de
   confirmação e a tabela de quartos é atualizada na hora;

d) ~~não cálculo de taxa de ocupação~~ — **corrigido**: a taxa de ocupação é
   calculada e exibida na aba de reservas;

e) ~~campos de data de início e término de reserva com tamanho reduzido (assim
   como botão de confirmação de reserva)~~ — **corrigido**: os campos de data
   e o botão de criação de reserva ganharam largura fixa maior;

f) ~~não atualização imediata de reserva após check-in~~ — **corrigido**: as
   tabelas de reservas e quartos são redesenhadas imediatamente após o
   check-in, mantendo a reserva selecionada com o novo status;

g) ~~possibilidade de cancelamento de reserva após realização de check-in~~ —
   **corrigido**: o cancelamento só é aceito para reservas ainda PENDENTES;
   após o check-in a operação é negada com aviso;

h) ~~não confirmação, quando for o caso, de inclusão em fila de reserva via
   caixa de diálogo de alerta~~ — **corrigido**: quando o quarto está
   indisponível, um alerta confirma a inclusão do hóspede na fila de espera;

i) ~~não notificação, via caixa de diálogo de alerta, de disponibilidade de
   quarto para hóspede em fila de reserva~~ — **corrigido**: no check-out,
   o próximo da fila é notificado via alerta e recebe o quarto liberado;

j) ~~aparente não definição de prazo de cancelamento~~ — **corrigido**:
   cancelamento exige antecedência mínima de 24h da data de entrada;

l) ~~ausência de histórico de hospedagem de cada hóspede~~ — **corrigido**:
   a aba de hóspedes exibe o histórico de estadias do hóspede selecionado.
