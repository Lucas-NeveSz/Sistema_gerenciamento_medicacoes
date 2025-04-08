# Sistema de Gerenciamento de Farmácia (JavaFX)

Este é um sistema desktop desenvolvido em Java utilizando JavaFX para controle de estoque de medicamentos em uma farmácia. O sistema permite cadastro, consulta, exclusão, alteração e geração de relatórios sobre os medicamentos, incluindo informações de fornecedores.

---

## Tecnologias Utilizadas

- Java 11+
- JavaFX
- FXML
- Scene Builder (opcional para edição visual)
- Padrão MVC
- Manipulação de arquivos CSV
- Collections e Stream API
- Validação de dados e boas práticas de POO

---

## Funcionalidades

### Medicamentos
- Cadastro de novos medicamentos
- Consulta por código
- Alteração e exclusão de registros
- Visualização em tabela
- Campos validados (nome, código, validade, preço, etc.)
- Indicação de medicamentos controlados

### Fornecedores
- Associação de fornecedores aos medicamentos
- Seleção via ComboBox
- Dados completos de CNPJ, razão social, contato e localização

### Relatórios
- Medicamentos próximos ao vencimento (30 dias)
- Medicamentos com estoque baixo (< 5 unidades)
- Valor total do estoque por fornecedor
- Comparativo de medicamentos controlados vs não controlados

---

## Estrutura do Projeto
├───src
│   └───main
│       ├───java
│       │   │   module-info.java
│       │   │
│       │   ├───application
│       │   │       Main.java
│       │   │
│       │   ├───controller
│       │   │       MainController.java
│       │   │       MedicamentoController.java
│       │   │       RelatorioController.java
│       │   │
│       │   ├───model
│       │   │       Fornecedor.java
│       │   │       Medicamento.java
│       │   │
│       │   ├───service
│       │   │       ArquivoService.java
│       │   │       FornecedorService.java
│       │   │       MedicamentoService.java
│       │   │
│       │   └───util
│       │           DateUtil.java
│       │           ValidacaoUtil.java
│       │
│       └───resources
│           └───view
│                   main.fxml
│                   medicamento.fxml
│                   relatorio.fxml
│
└───target
    ├───classes
    │   │   module-info.class
    │   │
    │   ├───application
    │   │       Main.class
    │   │
    │   ├───controller
    │   │       MainController.class
    │   │       MedicamentoController.class
    │   │       RelatorioController.class
    │   │
    │   ├───model
    │   │       Fornecedor.class
    │   │       Medicamento.class
    │   │
    │   ├───service
    │   │       ArquivoService.class
    │   │       FornecedorService.class
    │   │       MedicamentoService.class
    │   │
    │   ├───util
    │   │       DateUtil.class
    │   │       ValidacaoUtil.class
    │   │
    │   └───view
    │           main.fxml
    │           medicamento.fxml
    │           relatorio.fxml
    │
    └───generated-sources
        └───annotations


---

## Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/Lucas-NeveSz/Sistema_gerenciamento_medicacoes.git
2. Importe o projeto em sua IDE (IntelliJ, Eclipse ou VS Code com suporte JavaFX).

3. Configure a biblioteca JavaFX no seu projeto.

4. Execute a classe application.Main.

## Validações Implementadas
- Código do medicamento: 4 caracteres alfanuméricos

- Nome com mínimo de 3 letras

- Data de validade não pode ser passada

- Quantidade e preço devem ser positivos

- Fornecedor obrigatório

## Autor:
Email: lucasneves2001@gmail.com  
GitHub:Lucas-NeveSz
