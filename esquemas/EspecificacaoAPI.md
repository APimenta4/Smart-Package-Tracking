# Especificação da API da Aplicação de Monitorização

IPLeiria - ESTG, Desenvolvimento de Aplicações Empresariais

- Afonso Pimenta, 2223048
- Isa Santos, 2221408
- Rodrigo Camarada, 2221486
- Tiago Garcia, 2222277

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
    - [EP18 Consultar Sensor](#ep18-consultar-sensor)
    - [EP19 Consultar o Seu Sensor](#ep19-consultar-o-seu-sensor)
    - [EP20 Consultar as Leituras de um Sensor](#ep20-consultar-as-leituras-de-um-sensor)
  
  - [Leituras](#leituras)
    - [EP21 Enviar Leituras](#ep21-enviar-leituras)
    - [EP22 Consultar Todas as Leituras](#ep22-consultar-todas-as-leituras)
    - [EP23 Consultar Todas as Suas Leituras](#ep23-consultar-todas-as-suas-leituras)

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
/monitoring/api/orders
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "clientCode": "client",
  "code": "O1",
  "volumes": [
    {
      "code": "V1",
      "packageType": "ISOTERMIC",
      "products": [
        {
          "code": "P01",
          "quantity": 100
        },
        ...
      ],
      "sensors": [
        {
          "code": "S1",
          "type": "TEMPERATURE"
        },
        ...
      ]
    },
    ...
  ]
}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "clientCode": "client",
  "code": "O1",
  "volumes": [
    {
      "code": "V1",
      "orderCode": "O1",
      "packageType": "ISOTERMIC",
      "products": [
        {
          "code": "P01",
          "description": "Banana",
          "packageType": "NONE",
          "quantity": 100
        }
        ...
      ],
      "sensors": [
        {
          "code": "S1",
          "type": "TEMPERATURE",
          "volumeCode": "V1"
        },
        ...
      ],
      "readyDate": "2024-12-16T21:13:59.889Z[UTC]",
      "status": "READY_FOR_PICKUP"
    },
    ...
  ]
}
```

---

#### `EP02` Consultar Todas as Encomendas

Um utilizador, autenticado como gestor, consulta todas as encomendas.

**HTTP GET** para o sítio:

```text
/monitoring/api/orders
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "clientCode": "client",
    "code": "O1",
    "volumes": [
      {
        "code": "V1",
        "orderCode": "O1",
        "packageType": "ISOTERMIC",
        "products": [
          {
            "code": "P01",
            "description": "Banana",
            "packageType": "NONE",
            "quantity": 100
          }
          ...
        ],
        "sensors": [
          {
            "code": "S1",
            "type": "TEMPERATURE",
            "volumeCode": "V1"
          },
          ...
        ],
        "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
        "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
        "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
        "status": "DELIVERED"
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
/monitoring/api/orders
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "clientCode": "client",
    "code": "O1",
    "volumes": [
      {
        "code": "V1",
        "orderCode": "O1",
        "packageType": "ISOTERMIC",
        "products": [
          {
            "code": "P01",
            "description": "Banana",
            "packageType": "NONE",
            "quantity": 100
          }
          ...
        ],
        "sensors": [
          {
            "code": "S1",
            "type": "TEMPERATURE",
            "volumeCode": "V1"
          },
          ...
        ],
        "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
        "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
        "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
        "status": "DELIVERED"
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
/monitoring/api/orders/{orderCode}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "clientCode": "client",
  "code": "O1",
  "volumes": [
    {
      "code": "V1",
      "orderCode": "O1",
      "packageType": "ISOTERMIC",
      "products": [
        {
          "code": "P01",
          "description": "Banana",
          "packageType": "NONE",
          "quantity": 100
        }
        ...
      ],
      "sensors": [
        {
          "code": "S1",
          "type": "TEMPERATURE",
          "volumeCode": "V1"
        },
        ...
      ],
      "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
      "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
      "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
      "status": "DELIVERED"
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
/monitoring/api/orders/{orderCode}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "clientCode": "client",
  "code": "O1",
  "volumes": [
    {
      "code": "V1",
      "orderCode": "O1",
      "packageType": "ISOTERMIC",
      "products": [
        {
          "code": "P01",
          "description": "Banana",
          "packageType": "NONE",
          "quantity": 100
        }
        ...
      ],
      "sensors": [
        {
          "code": "S1",
          "type": "TEMPERATURE",
          "volumeCode": "V1"
        },
        ...
      ],
      "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
      "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
      "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
      "status": "DELIVERED"
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
/monitoring/api/orders/{orderCode}/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "code": "V1",
    "orderCode": "O1",
    "packageType": "ISOTERMIC",
    "products": [
      {
        "code": "P01",
        "description": "Banana",
        "packageType": "NONE",
        "quantity": 100
      },
      ...
    ],
    "sensors": [
      {
        "code": "S1",
        "type": "TEMPERATURE",
        "volumeCode": "V1"
      },
      ...
    ],
    "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
    "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
    "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
    "status": "DELIVERED"
  },
  ...
]
```

---

#### `EP07` Consultar Volumes Associados á Sua Encomenda

Um utilizador, autenticado como cliente, consulta todos os volumes de uma das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitoring/api/orders/{orderCode}/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "code": "V1",
    "orderCode": "O1",
    "packageType": "ISOTERMIC",
    "products": [
      {
        "code": "P01",
        "description": "Banana",
        "packageType": "NONE",
        "quantity": 100
      },
      ...
    ],
    "sensors": [
      {
        "code": "S1",
        "type": "TEMPERATURE",
        "volumeCode": "V1"
      },
      ...
    ],
    "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
    "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
    "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
    "status": "DELIVERED"
  },
  ...
]
```

---

#### `EP08` Consultar Leituras Associadas a Uma Encomenda

Um utilizador, autenticado como gestor, consulta todas as leituras dos sensores de uma encomenda.

**HTTP GET** para o sítio:

```text
/monitoring/api/orders/{orderCode}/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "sensor": {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    "readings": [
      {
        "temperature": 23.56,
        "timestamp": 1737062019452
      },
      ...
    ]
  },
  ...
]
```

---

#### `EP09` Consultar Leituras Associadas á Sua Encomenda

Um utilizador, autenticado como cliente, consulta as leituras dos sensores de uma das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitoring/api/orders/{orderCode}/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "sensor": {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    "readings": [
      {
        "temperature": 23.56,
        "timestamp": 1737062019452
      },
      ...
    ]
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
/monitoring/api/volumes
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "code": "V1",
  "orderCode": "O1",
  "packageType": "ISOTERMIC",
  "products": [
    {
      "code": "P01",
      "quantity": 100
    },
    ...
  ],
  "sensors": [
    {
      "code": "S1",
      "type": "TEMPERATURE"
    },
    ...
  ]
}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "code": "V1",
  "orderCode": "O1",
  "packageType": "ISOTERMIC",
  "products": [
    {
      "code": "P01",
      "description": "Banana",
      "packageType": "NONE",
      "quantity": 100
    },
    ...
  ],
  "sensors": [
    {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    ...
  ],
  "readyDate": "2024-12-16T21:13:59.889Z[UTC]",
  "status": "READY_FOR_PICKUP"
}
```

---

#### `EP11` Atualizar Estado Volume

Um utilizador, autenticado como funcionário de logística, atualiza o estado de um volume.

**HTTP PATCH** para o sítio:

```text
/monitoring/api/volumes/{volumeCode}
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "status": "ENTREGUE"
}
```

---

#### `EP12` Consultar Todos os Volumes

Um utilizador, autenticado como gestor, consulta todos os volumes.

**HTTP GET** para o sítio:

```text
/monitoring/api/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "code": "V1",
    "orderCode": "O1",
    "packageType": "ISOTERMIC",
    "products": [
      {
        "code": "P01",
        "description": "Banana",
        "packageType": "NONE",
        "quantity": 100
      },
      ...
    ],
    "sensors": [
      {
        "code": "S1",
        "type": "TEMPERATURE",
        "volumeCode": "V1"
      },
      ...
    ],
    "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
    "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
    "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
    "status": "DELIVERED"
  },
  ...
]
```

---

#### `EP13` Consultar Todos os Seus Volumes

Um utilizador, autenticado como cliente, consulta todos os seus volumes.

**HTTP GET** para o sítio:

```text
/monitoring/api/volumes
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "code": "V1",
    "orderCode": "O1",
    "packageType": "ISOTERMIC",
    "products": [
      {
        "code": "P01",
        "description": "Banana",
        "packageType": "NONE",
        "quantity": 100
      },
      ...
    ],
    "sensors": [
      {
        "code": "S1",
        "type": "TEMPERATURE",
        "volumeCode": "V1"
      },
      ...
    ],
    "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
    "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
    "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
    "status": "DELIVERED"
  },
  ...
]
```

---

#### `EP14` Consultar Volume

Um utilizador, autenticado como gestor, consulta os detalhes de um volume.

**HTTP GET** para o sítio:

```text
/monitoring/api/volumes/{volumeCode}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "code": "V1",
  "orderCode": "O1",
  "packageType": "ISOTERMIC",
  "products": [
    {
      "code": "P01",
      "description": "Banana",
      "packageType": "NONE",
      "quantity": 100
    },
    ...
  ],
  "sensors": [
    {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    ...
  ],
  "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
  "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
  "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
  "status": "DELIVERED"
}
```

---

#### `EP15` Consultar o Seu Volume

Um utilizador, autenticado como cliente, consulta os detalhes de um dos seus volumes.

**HTTP GET** para o sítio:

```text
/monitoring/api/volumes/{volumeCode}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "code": "V1",
  "orderCode": "O1",
  "packageType": "ISOTERMIC",
  "products": [
    {
      "code": "P01",
      "description": "Banana",
      "packageType": "NONE",
      "quantity": 100
    },
    ...
  ],
  "sensors": [
    {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    ...
  ],
  "readyDate": "2024-12-16T10:13:59.889Z[UTC]",
  "shippedDate": "2024-12-16T16:24:10.798Z[UTC]",
  "deliveredDate": "2024-12-20T11:54:59.978Z[UTC]",
  "status": "DELIVERED"
}
```

---

#### `EP16` Consultar Leituras Associados a Um Volume

Um utilizador, autenticado como gestor, consulta as leituras dos sensores de um volume.

**HTTP GET** para o sítio:

```text
/monitoring/api/volumes/{volumeCode}/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "sensor": {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    "readings": [
      {
        "temperature": 23.56,
        "timestamp": 1737062019452
      },
      ...
    ]
  },
  ...
]
```

---

#### `EP17` Consultar Leituras Associados ao Seu Volume

Um utilizador, autenticado como cliente, consulta as leituras dos sensores de um dos seus volumes.

**HTTP GET** para o sítio:

```text
/monitoring/api/volumes/{volumeCode}/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "sensor": {
      "code": "S1",
      "type": "TEMPERATURE",
      "volumeCode": "V1"
    },
    "readings": [
      {
        "temperature": 23.56,
        "timestamp": 1737062019452
      },
      ...
    ]
  },
  ...
]
```

---

### Sensores

#### `EP18` Consultar Sensor

Um utilizador, autenticado como gestor, consulta os detalhes de um sensor.

**HTTP GET** para o sítio:

```text
/monitoring/api/sensors/{sensorCode}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "code": "S1",
  "type": "TEMPERATURE",
  "volumeCode": "V1"
}
```

---

#### `EP19` Consultar o Seu Sensor

Um utilizador, autenticado como cliente, consulta os detalhes de um dos seus sensors.

**HTTP GET** para o sítio:

```text
/monitoring/api/sensors/{sensorCode}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "code": "S1",
  "type": "TEMPERATURE",
  "volumeCode": "V1"
}
```

---

#### `EP20` Consultar as Leituras de Um Sensor

Um utilizador, autenticado como gestor, consulta todas as leituras de um sensor em específico.

**HTTP GET** para o sítio:

```text
/monitoring/api/sensors/{sensorCode}/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
{
  "sensor": {
    "code": "S1",
    "type": "TEMPERATURE",
    "volumeCode": "V1"
  },
  "readings": [
    {
      "temperature": 23.56,
      "timestamp": 1737062019452
    },
    ...
  ]
}
```

---

### Leituras

#### `EP21` Enviar Leituras

Um sensor envia uma nova leitura para o sistema de monitorização.

**HTTP POST** para o sítio:

```text
/monitoring/api/readings
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "sensorCode": "S1",
  "temperature": 23.56
}
ou
{
  "sensorCode": "S2",
  "latitude": -50.61,
  "longitude": 165.97
}
ou
{
  "sensorCode": "S3",
  "acceleration": 1.5
}
```

---

#### `EP22` Consultar Todas as Leituras

Um utilizador, autenticado como gestor, consulta todas as leituras dos sensores.

**HTTP GET** para o sítio:

```text
/monitoring/api/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "sensorCode": "S1",
    "temperature": 24,
    "timestamp": 1737062019452,
    "type": "TEMPERATURE",
    "volumeCode": "V1"
  },
  {
    "latitude": -50.61,
    "longitude": 165.97,
    "sensorCode": "S2",
    "timestamp": 1737062019454,
    "type": "LOCATION",
    "volumeCode": "V1"
  },
  {
    "acceleration": 1.5,
    "sensorCode": "S3",
    "timestamp": 1737062019458,
    "type": "ACCELERATION",
    "volumeCode": "V2"
  },
  ...
]
```

#### `EP23` Consultar Todas as Suas Leituras

Um utilizador, autenticado como cliente, consulta todas as leituras dos sensores associados aos volumes das suas encomendas.

**HTTP GET** para o sítio:

```text
/monitoring/api/readings
```

A resposta devolvida por este recurso segue o formato JSON:

```json
[
  {
    "sensorCode": "S1",
    "temperature": 24,
    "timestamp": 1737062019452,
    "type": "TEMPERATURE",
    "volumeCode": "V1"
  },
  {
    "latitude": -50.61,
    "longitude": 165.97,
    "sensorCode": "S2",
    "timestamp": 1737062019454,
    "type": "LOCATION",
    "volumeCode": "V1"
  },
  {
    "acceleration": 1.5,
    "sensorCode": "S3",
    "timestamp": 1737062019458,
    "type": "ACCELERATION",
    "volumeCode": "V2"
  },
  ...
]
```
