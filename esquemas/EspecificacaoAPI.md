# Especificação da API da Aplicação de Monitorização

IPLeiria - ESTG, Desenvolvimento de Aplicações Empresariais

Entrega Intermédia

- Afonso Pimenta, 2223048
- Isa Santos, 2221408
- Rodrigo Camarada, 2221486
- Tiago Garcia, 2222277

## TODOS

- Codigos das relações não devem parecer nos json.
  
  Não me faz sentido mas o stor disse, vou so deixar aqui a nota.
  Em alguns endpoints realmente não é preciso mas nos endpoints especificos
  dessas entidades são necessarios.
  Exemplo do que seria no [EP07](#ep07-consultar-volumes-associados-á-sua-encomenda)

  ```json
  // exemplo embalagem
  {
    "codigo": 441,
    "volume": {
      "codigo": 23,
      "codigoEmbalagem": 23,// remover este
    }
  }
  ```

- Codigo dos volumes nas leituras

  O stor reclamou, mas concordou quando lhe disse que faria sentido se tiveres a ver uma leitura pelo endoint das leituras vai dar jeito no front end para n teres de ir á pagina do sensor para depois poderes ir ao volume.

## Indíce

- [Tipos de Usuários e Permissões](#tipos-de-usuários-e-permissões)
  - [Funcionário de Logística](#funcionário-de-logística)
  - [Gestor](#gestor)
  - [Cliente](#cliente)

- [EndPoints](#endpoints)
  - [Encomendas](#encomendas)
    - [EP01 Criar Encomenda](#ep01-criar-encomenda)
    - [EP02 Consultar Todas as Encomendas](#ep02-consultar-todas-as-encomendas)
    - [EP03 Consultar Todas as Suas Encomendas](#ep03-consultar-todas-as-suas-encomendas)
    - [EP04 Consultar Uma Encomenda](#ep04-consultar-uma-encomenda)
    - [EP05 Consultar a Sua Encomenda](#ep05-consultar-a-sua-encomenda)
    - [EP06 Consultar Volumes Associados a Uma Ancomenda](#ep06-consultar-volumes-associados-a-uma-encomenda)
    - [EP07 Consultar Volumes Associados á Sua Encomenda](#ep07-consultar-volumes-associados-á-sua-encomenda)
    - [EP08 Consultar Leituras Associadas a Uma Encomenda](#ep08-consultar-leituras-associadas-a-uma-encomenda)
    - [EP09 Consultar Leituras Associadas á Sua Encomenda](#ep09-consultar-leituras-associadas-á-sua-encomenda)
  
  - [Volumes](#volumes)
    - [EP10 Adicionar Volume a Uma Encomenda](#ep10-adicionar-volume-a-uma-encomenda)
    - [EP11 Atualizar Estado Volume](#ep11-atualizar-estado-volume)
    - [EP12 Consultar Todos os Volumes](#ep12-consultar-todos-os-volumes)
    - [EP13 Consultar Todos os Seus Volumes](#ep13-consultar-todos-os-seus-volumes)
    - [EP14 Consultar Volume](#ep14-consultar-volume)
    - [EP15 Consultar o Seu Volume](#ep15-consultar-o-seu-volume)
    - [EP16 Consultar Leituras Associadas a Um Volume](#ep16-consultar-leituras-associados-a-um-volume)
    - [EP17 Consultar Leituras Associadas ao Seu Volume](#ep17-consultar-leituras-associados-ao-seu-volume)

  - [Sensores](#sensores)
    - [EP18 Consultar as Leituras de um Sensor](#ep18-consultar-as-leituras-de-um-sensor)
  
  - [Leituras](#leituras)
    - [EP19 Enviar Leituras](#ep19-enviar-leituras)
    - [EP20 Consultar Todas as Leituras](#ep20-consultar-todas-as-leituras)
    - [EP21 Consultar Todas as Suas Leituras](#ep21-consultar-todas-as-suas-leituras)

### Tipos de Usuários e Permissões

#### Funcionário de Logística

É permitido ao funcionário de logística:

- criar uma nova encomenda para monitorização.
- adicionar volumes a encomendas existentes.
- modificar o estado dos volumes para "Pendente", "Em Trânsito", "Entregue", etc.

---

#### Gestor

É permitido ao gestor:

- consultar todas as encomendas no sistema.
- consultar volumes de todas as encomendas.
- consultar as leituras de todos os sensores.
- consultar as leituras dos sensores associados a qualquer encomenda.

---

#### Cliente

É permitido ao cliente:

- consultar detalhes das suas encomendas.
- consultar detalhes dos volumes das suas encomendas.
- consultar as leituras dos sensores associados às suas encomendas.

## EndPoints

### Encomendas

#### `EP01` Criar Encomenda

Um utilizador, autenticado como funcionário de logística, adiciona uma nova encomenda com um volume para monitorização.

**HTTP POST** para o sítio:

```text
/monitorizacao/api/encomendas
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "codigo": 441,
  "codigoCliente":65,
  "volume": {
    "codigo": 23,
    "tipoEmbalagem": "FRAGIL",
    "produtos": [
      {
        "codigo": 82737,
        "unidades": 5
      },
      ...
    ], 
    "sensores": [
      {
        "codigo": 432,
        "tipo": "ACELERACAO"
      },
      ...
    ], 
  }
}
```

---

#### `EP02` Consultar Todas as Encomendas

Um utilizador, autenticado como gestor, consulta todas as encomendas.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {        
    "codigo": 2,
    "codigoCliente": 65,
    "volumes": [
      {
        "codigo": 42,
        "codigoEncomenda": 2,
        "estado": "ENTREGUE",
        "dataEnvio": "28/12/2024",
        "dataEntrega": "08/01/2024",
        "tipoEmbalagem": "FRAGIL",
        "produtos": [
          {
            "codigo":1256,
            "codigoVolume": 42, //TODO chave composta?
            "descricao": "LG Smart TV LED UHD 4K",
            "categoria": "ELETRODOMESTICOS",
            "unidades": 5
          },
          ...
        ],
        "sensores": [
          {
            "codigo": 345,
            "codigoVolume": 42,
            "tipo": "ACELERACAO"
          },
          ...
        ],
      },
      ...
    ]
  },
  ...
]
```

---

#### `EP03` Consultar Todas as Suas Encomendas

Um utilizador, autenticado como cliente, consulta as suas encomendas.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {        
    "codigo": 2,
    "codigoCliente": 65,
    "volumes": [
      {
        "codigo": 42,
        "codigoEncomenda": 2,
        "estado": "ENTREGUE",
        "dataEnvio": "28/12/2024",
        "dataEntrega": "08/01/2024",
        "tipoEmbalagem": "FRAGIL",
        "produtos": [
          {
            "codigo":1256,
            "codigoVolume": 42, //TODO chave composta?
            "descricao": "LG Smart TV LED UHD 4K",
            "categoria": "ELETRODOMESTICOS",
            "unidades": 5
          },
          ...
        ],
        "sensores": [
          {
            "codigo": 345,
            "codigoVolume": 42,
            "tipo": "ACELERACAO"
          },
          ...
        ],
      },
      ...
    ]
  },
  ...
]
```

---

#### `EP04` Consultar Uma Encomenda

Um utilizador, autenticado como gestor, consulta detalhes de uma encomenda específica.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas/{codigoEncomenda}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{        
  "codigo": 2,
  "codigoCliente": 65,
  "volumes": [
    {
      "codigo": 42,
      "codigoEncomenda": 2,
      "estado": "ENTREGUE",
      "dataEnvio": "28/12/2024",
      "dataEntrega": "08/01/2024",
      "tipoEmbalagem": "FRAGIL",
      "produtos": [
        {
          "codigo":1256,
          "codigoVolume": 42,//TODO chave composta?
          "descricao": "LG Smart TV LED UHD 4K",
          "categoria": "ELETRODOMESTICOS",
          "unidades": 5
        },
        ...
      ],
      "sensores": [
        {
          "codigo": 345,
          "codigoVolume": 42,
          "tipo": "ACELERACAO"
        },
        ...
      ],
    },
    ...
  ]
}
```

---

#### `EP05` Consultar a Sua Encomenda

Um utilizador, autenticado como cliente, consulta detalhes de uma das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas/{codigoEncomenda}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{        
  "codigo": 2,
  "codigoCliente": 65,
  "volumes": [
    {
      "codigo": 42,
      "codigoEncomenda": 2,
      "estado": "ENTREGUE",
      "dataEnvio": "28/12/2024",
      "dataEntrega": "08/01/2024",
      "tipoEmbalagem": "FRAGIL",
      "produtos": [
        {
          "codigo":1256,
          "codigoVolume": 42,//TODO chave composta?
          "descricao": "LG Smart TV LED UHD 4K",
          "categoria": "ELETRODOMESTICOS",
          "unidades": 5
        },
        ...
      ],
      "sensores": [
        {
          "codigo": 345,
          "codigoVolume": 42,
          "tipo": "ACELERACAO"
        },
        ...
      ],
    },
    ...
  ]
}
```

---

#### `EP06` Consultar Volumes Associados a Uma Encomenda

Um utilizador, autenticado como gestor, consulta todos os volumes de uma encomenda.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas/{codigoEncomenda}/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigo": 42,
    "codigoEncomenda": 2,
    "estado": "ENTREGUE",
    "dataEnvio": "28/12/2024",
    "dataEntrega": "08/01/2024",
    "tipoEmbalagem": "FRAGIL",
    "produtos": [
      {
        "codigo":1256,
        "codigoVolume": 42,//TODO chave composta?
        "descricao": "LG Smart TV LED UHD 4K",
        "categoria": "ELETRODOMESTICOS",
        "unidades": 5
      },
      ...
    ],
    "sensores": [
      {
        "codigo": 345,
        "codigoVolume": 42,
        "tipo": "ACELERACAO"
      },
      ...
    ],
  },
  ...
]
```

---

#### `EP07` Consultar Volumes Associados á Sua Encomenda

Um utilizador, autenticado como cliente, consulta todos os volumes de uma das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas/{codigoEncomenda}/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigo": 42,
    "codigoEncomenda": 2,//? neste n é preciso mas no api/volumes é
    "estado": "ENTREGUE",
    "dataEnvio": "28/12/2024",
    "dataEntrega": "08/01/2024",
    "tipoEmbalagem": "FRAGIL",
    "produtos": [
      {
        "codigo": 1256,
        "descricao": "LG Smart TV LED UHD 4K",
        "categoria": "ELETRODOMESTICOS",
        "unidades": 5
      },
      ...
    ],
    "sensores": [
      {
        "codigo": 345,
        "tipo": "ACELERACAO"
      },
      ...
    ]
  },
  ...
]
```

---

#### `EP08` Consultar Leituras Associadas a Uma Encomenda

Um utilizador, autenticado como gestor, consulta todas as leituras dos sensores de uma encomenda.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas/{codigoEncomenda}/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigo": 3453,
    "codigoVolume": 42,
    "tipo": "GPS",
    "latitude": -50.61,
    "longitude": 165.97,
    "timestamp":"2024-12-28 19:30:23"
  },
  {
    "codigo": 3454,
    "codigoVolume": 42,
    "tipo": "TEMPERATURA",
    "temperatura": 15,
    "timestamp":"2024-12-28 19:25:20"
  },
  {
    "codigo": 3455,
    "codigoVolume": 42,
    "tipo": "ACELERACAO",
    "aceleracao": 6,
    "timestamp":"2024-12-27 19:18:41"
  },
  ...
]
```

---

#### `EP09` Consultar Leituras Associadas á Sua Encomenda

Um utilizador, autenticado como cliente, consulta as leituras dos sensores de uma das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/encomendas/{codigoEncomenda}/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigo": 3453,
    "codigoVolume": 42,
    "tipo": "GPS",
    "latitude": -50.61,
    "longitude": 165.97,
    "timestamp":"2024-12-28 19:30:23"
  },
  {
    "codigo": 3454,
    "codigoVolume": 42,
    "tipo": "TEMPERATURA",
    "temperatura": 15,
    "timestamp":"2024-12-28 19:25:20"
  },
  {
    "codigo": 3455,
    "codigoVolume": 42,
    "tipo": "ACELERACAO",
    "aceleracao": 6,
    "timestamp":"2024-12-27 19:18:41"
  },
  ...
]
```

---

### Volumes

#### `EP10` Adicionar Volume a Uma Encomenda

Um utilizador, autenticado como funcionário de logística, adiciona um novo volume a uma encomenda.

**HTTP POST** para o sítio:

```text
/monitorizacao/api/volumes
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "codigo": 42,
  "codigoEncomenda": 2,
  "tipoEmbalagem": "FRAGIL",
  "produtos": [
    {
      "codigo":1256,
      "unidades": 5
    },
    ...
  ],
  "sensores": [
    {
      "codigo": 345,
      "tipo": "ACELERACAO"
    },
    ...
  ]
}
```

---

#### `EP11` Atualizar Estado Volume

Um utilizador, autenticado como funcionário de logística, atualiza o estado de um volume.

**HTTP PATCH** para o sítio:

```text
/monitorizacao/api/volumes/{codigoVolume}
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "estado": "ENTREGUE"
}
```

---

#### `EP12` Consultar Todos os Volumes

Um utilizador, autenticado como gestor, consulta todos os volumes.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigo": 42,
    "codigoEncomenda": 2, 
    "estado": "ENTREGUE",
    "dataEnvio": "28/12/2024",
    "dataEntrega": "08/01/2024",
    "tipoEmbalagem": "FRAGIL",
    "produtos": [
      {
        "codigo":1256,
        "codigoVolume": 42,//TODO chave composta?
        "descricao": "LG Smart TV LED UHD 4K",
        "categoria": "ELETRODOMESTICOS",
        "unidades": 5
      },
      ...
    ],          
    "sensores": [
      {
        "codigo": 345,
        "codigoVolume": 42,
        "tipo": "ACELERACAO"
      },
      ...
    ], 
  },
  ...
]
```

---

#### `EP13` Consultar Todos os Seus Volumes

Um utilizador, autenticado como cliente, consulta todos os seus volumes.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigo": 42,
    "codigoEncomenda": 2, 
    "estado": "ENTREGUE",
    "dataEnvio": "28/12/2024",
    "dataEntrega": "08/01/2024",
    "tipoEmbalagem": "FRAGIL",
    "produtos": [
      {
        "codigo":1256,
        "codigoVolume": 42,//TODO chave composta?
        "descricao": "LG Smart TV LED UHD 4K",
        "categoria": "ELETRODOMESTICOS",
        "unidades": 5,
      },
      ...
    ],          
    "sensores": [
      {
        "codigo": 345,
        "codigoVolume": 42,
        "tipo": "ACELERACAO"
      },
      ...
    ], 
  },
  ...
]
```

---

#### `EP14` Consultar Volume

Um utilizador, autenticado como gestor, consulta os detalhes de um volume.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/volumes/{codigoVolume}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "codigo": 42,
  "codigoEncomenda": 2, 
  "estado": "ENTREGUE",
  "dataEnvio": "28/12/2024",
  "dataEntrega": "08/01/2024",
  "tipoEmbalagem": "FRAGIL",
  "produtos": [
    {
      "codigo":1256,
      "codigoVolume": 42,//TODO chave composta?
      "descricao": "LG Smart TV LED UHD 4K",
      "categoria": "ELETRODOMESTICOS",
      "unidades": 5
    },
    ...
  ],          
  "sensores": [
    {
      "codigo": 345,
      "codigoVolume": 42,
      "tipo": "ACELERACAO"
    },
    ...
  ], 
}
```

---

#### `EP15` Consultar o Seu Volume

Um utilizador, autenticado como cliente, consulta os detalhes de um dos seus volumes.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/volumes/{codigoVolume}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "codigo": 42,
  "codigoEncomenda": 2, 
  "estado": "ENTREGUE",
  "dataEnvio": "28/12/2024",
  "dataEntrega": "08/01/2024",
  "tipoEmbalagem": "FRAGIL",
  "produtos": [
    {
        "codigo":1256,
        "codigoVolume": 42,//TODO chave composta?
        "descricao": "LG Smart TV LED UHD 4K",
        "categoria": "ELETRODOMESTICOS",
        "unidades": 5
    },
    ...
  ],          
  "sensores": [
    {
      "codigo": 345,
      "codigoVolume": 42,
      "tipo": "ACELERACAO"
    },
    ...
  ], 
}
```

---

#### `EP16` Consultar Leituras Associados a Um Volume

Um utilizador, autenticado como gestor, consulta as leituras dos sensores de um volume.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/volumes/{codigoVolume}/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigoSensor": 3453,
    "codigoVolume": 42,
    "tipo": "GPS",
    "latitude": -50.61,
    "longitude": 165.97,
    "timestamp":"2024-12-28 19:30:23"
  },
  {
    "codigoSensor": 3454,
    "codigoVolume": 42,
    "tipo": "TEMPERATURA",
    "temperatura": 15,
    "timestamp":"2024-12-28 19:25:20"
  },
  {
    "codigoSensor": 3455,
    "codigoVolume": 42,
    "tipo": "ACELERACAO",
    "aceleracao": 6,
    "timestamp":"2024-12-27 19:18:41"
  },
  ...
]
```

---

#### `EP17` Consultar Leituras Associados ao Seu Volume

Um utilizador, autenticado como cliente, consulta as leituras dos sensores de um dos seus volumes.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/volumes/{codigoVolume}/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigoSensor": 3453,
    "codigoVolume": 42,
    "tipo": "GPS",
    "latitude": -50.61,
    "longitude": 165.97,
    "timestamp":"2024-12-28 19:30:23"
  },
  {
    "codigoSensor": 3453,
    "codigoVolume": 42,
    "tipo": "TEMPERATURA",
    "temperatura": 15,
    "timestamp":"2024-12-28 19:25:20"
  },
  {     
    "codigoSensor": 3453,
    "codigoVolume": 42,
    "tipo": "ACELERACAO",
    "aceleracao": 6,
    "timestamp":"2024-12-27 19:18:41"
  },
  ...
]
```

---

### Sensores

#### `EP18` Consultar as Leituras de Um Sensor

Um utilizador, autenticado como gestor, consulta todas as leituras de um sensor em específico.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/sensores/{codigoSensor}/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "sensor":
    {
      "codigo": 3454,
      "codigoVolume": 42,
      "tipo": "TEMPERATURA"
    },
  "leituras":[
    {
      "temperatura": 15,
      "timestamp":"2024-12-28 19:30:23"
    },
    {
      "temperatura": 15,
      "timestamp":"2024-12-28 19:25:20"
    },
    {
      "temperatura": 16,
      "timestamp":"2024-12-28 19:20:49"
    },
    ...
  ]
}
```

---

### Leituras

#### `EP19` Enviar Leituras

Um sensor envia uma nova leitura para o sistema de monitorização.

**HTTP POST** para o sítio:

```text
/monitorizacao/api/leituras
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "codigoSensor": 5321,
  "latitude": -50.61,
  "longitude": 165.97
}
ou
{
  "codigoSensor": 5331,
  "temperatura": 12
}
ou
{
  "codigoSensor": 5451,
  "aceleracao": 5
}
ou
...
```

---

#### `EP20` Consultar Todas as Leituras

Um utilizador, autenticado como gestor, consulta todas as leituras dos sensores.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigoSensor": 3453,
    "codigoVolume": 42,
    "tipo": "GPS",
    "latitude": -50.61,
    "longitude": 165.97,
    "timestamp":"2024-12-28 19:30:23"
  },
  {
    "codigoSensor": 3454,
    "codigoVolume": 42,
    "tipo": "TEMPERATURA",
    "temperatura": 15,
    "timestamp":"2024-12-28 19:25:20"
  },
  {
    "codigoSensor": 3455,
    "codigoVolume": 42,
    "tipo": "ACELERACAO",
    "aceleracao": 6,
    "timestamp":"2024-12-27 19:18:41"
  },
  ...
]
```

#### `EP21` Consultar Todas as Suas Leituras

Um utilizador, autenticado como cliente, consulta todas as leituras dos sensores associados aos volumes das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitorizacao/api/leituras
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "codigoSensor": 3453,
    "codigoVolume": 42,
    "tipo": "GPS",
    "latitude": -50.61,
    "longitude": 165.97,
    "timestamp":"2024-12-28 19:30:23"
  },
  {
    "codigoSensor": 3454,
    "codigoVolume": 42,
    "tipo": "TEMPERATURA",
    "temperatura": 15,
    "timestamp":"2024-12-28 19:25:20"
  },
  {
    "codigoSensor": 3455,
    "codigoVolume": 42,
    "tipo": "ACELERACAO",
    "aceleracao": 6,
    "timestamp":"2024-12-27 19:18:41"
  },
  ...
]
```