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

  - [Autenticação](#autenticação)
    - [EP01 Login](#ep01-login)
    - [EP02 Refresh token](#ep02-refresh-token)
    - [EP03 Atualizar password](#ep03-atualizar-password)
    - [EP04 Consultar a sua informaçao de utilizador](#ep04-consultar-a-sua-informaçao-de-utilizador)

  - [Encomendas](#encomendas)
    - [EP05 Criar Encomenda](#ep05-criar-encomenda)
    - [EP06 Consultar Todas as Encomendas](#ep06-consultar-todas-as-encomendas)
    - [EP07 Consultar Todas as Suas Encomendas](#ep07-consultar-todas-as-suas-encomendas)
    - [EP08 Consultar Uma Encomenda](#ep08-consultar-uma-encomenda)
    - [EP09 Consultar a Sua Encomenda](#ep09-consultar-a-sua-encomenda)
    - [EP10 Consultar Volumes Associados a Uma Ancomenda](#ep10-consultar-volumes-associados-a-uma-encomenda)
    - [EP11 Consultar Volumes Associados á Sua Encomenda](#ep11-consultar-volumes-associados-á-sua-encomenda)
    - [EP12 Consultar Leituras Associadas a Uma Encomenda](#ep12-consultar-leituras-associadas-a-uma-encomenda)
    - [EP13 Consultar Leituras Associadas á Sua Encomenda](#ep13-consultar-leituras-associadas-á-sua-encomenda)
  
  - [Volumes](#volumes)
    - [EP14 Adicionar Volume a Uma Encomenda](#ep14-adicionar-volume-a-uma-encomenda)
    - [EP15 Atualizar Estado Volume](#ep15-atualizar-estado-volume)
    - [EP16 Consultar Todos os Volumes](#ep16-consultar-todos-os-volumes)
    - [EP17 Consultar Todos os Seus Volumes](#ep17-consultar-todos-os-seus-volumes)
    - [EP18 Consultar Volume](#ep18-consultar-volume)
    - [EP19 Consultar o Seu Volume](#ep19-consultar-o-seu-volume)
    - [EP20 Consultar Leituras Associadas a Um Volume](#ep20-consultar-leituras-associados-a-um-volume)
    - [EP21 Consultar Leituras Associadas ao Seu Volume](#ep21-consultar-leituras-associados-ao-seu-volume)

  - [Sensores](#sensores)
    - [EP22 Consultar Sensor](#ep22-consultar-sensor)
    - [EP23 Consultar as Leituras de um Sensor](#ep23-consultar-as-leituras-de-um-sensor)
  
  - [Leituras](#leituras)
    - [EP24 Enviar Leituras](#ep24-enviar-leituras)
    - [EP25 Consultar Todas as Leituras](#ep25-consultar-todas-as-leituras)
    - [EP26 Consultar Todas as Suas Leituras](#ep26-consultar-todas-as-suas-leituras)

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

### Autenticação

#### `EP01` Login

Um utilizador autentica-se no sistemas.

**HTTP POST** para o sítio:

```text
/monitoring/api/auth/login
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "code":"client",
  "password":"123"
}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
asdfZXCVFDGQasdfq93.fdsQAdgaDFGhe3Lc3
```

---

#### `EP02` Refresh token

Um utilizador atualiza o seu token.

**HTTP POST** para o sítio:

```text
/monitoring/api/auth/refresh
```

A resposta devolvida por este recurso segue o formato JSON:

```json
asdfZXCVFDGQasdfq93.fdsQAdgaDFGhe3Lc3
```

---

#### `EP03` Atualizar password

Um utilizador autenticado atualiza a sua password.

**HTTP POST** para o sítio:

```text
/monitoring/api/auth/set-password
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "oldPassword":"123",
  "newPassword":"321",
  "confirmPassword":"321"
}
```

A resposta devolvida por este recurso segue o formato JSON:

```json
asdfZXCVFDGQasdfq93.fdsQAdgaDFGhe3Lc3
```

---

#### `EP04` Consultar a sua informaçao de utilizador

Um utilizador autenticado atualiza a sua password.

**HTTP POST** para o sítio:

```text
/monitoring/api/auth/set-password
```

O pedido enviado a este recurso segue o formato JSON:

```json
{
  "oldPassword":"123",
  "newPassword":"321",
  "confirmPassword":"321"
}
```

---

### Encomendas

#### `EP05` Criar Encomenda

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

#### `EP06` Consultar Todas as Encomendas

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

#### `EP07` Consultar Todas as Suas Encomendas

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

#### `EP08` Consultar Uma Encomenda

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

#### `EP09` Consultar a Sua Encomenda

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

#### `EP10` Consultar Volumes Associados a Uma Encomenda

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

#### `EP11` Consultar Volumes Associados á Sua Encomenda

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

#### `EP12` Consultar Leituras Associadas a Uma Encomenda

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

#### `EP13` Consultar Leituras Associadas á Sua Encomenda

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

#### `EP14` Adicionar Volume a Uma Encomenda

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

#### `EP15` Atualizar Estado Volume

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

#### `EP16` Consultar Todos os Volumes

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

#### `EP17` Consultar Todos os Seus Volumes

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

#### `EP18` Consultar Volume

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

#### `EP19` Consultar o Seu Volume

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

#### `EP20` Consultar Leituras Associados a Um Volume

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

#### `EP21` Consultar Leituras Associados ao Seu Volume

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

#### `EP22` Consultar Sensor

Um utilizador consulta os detalhes de um sensor.

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

#### `EP23` Consultar as Leituras de Um Sensor

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

#### `EP24` Enviar Leituras

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

#### `EP25` Consultar Todas as Leituras

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

#### `EP26` Consultar Todas as Suas Leituras

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
