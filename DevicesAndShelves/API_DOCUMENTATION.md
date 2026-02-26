# Device Management API Documentation

## Base URL
`http://localhost:8080/api/devices`

---

## API Endpoints

### 1. Create Device
**POST** `/api/devices`

Creates a new device with shelf positions.

**Request Body:**
```json
{
  "deviceName": "Server Rack A1",
  "partNumber": "SRV-RACK-001",
  "buildingName": "Building 1",
  "deviceType": "RACK",
  "totalShelfPositions": 42,
  "imageUrl": "https://example.com/image.jpg"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Device created successfully",
  "data": {
    "deviceId": "uuid-generated-here",
    "deviceName": "Server Rack A1",
    "partNumber": "SRV-RACK-001",
    "buildingName": "Building 1",
    "deviceType": "RACK",
    "totalShelfPositions": 42,
    "imageUrl": "https://example.com/image.jpg",
    "isDeleted": false
  }
}
```

---

### 2. Get Device by ID
**GET** `/api/devices/{deviceId}`

Retrieves a specific device by its ID.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "deviceId": "uuid-here",
    "deviceName": "Server Rack A1",
    "partNumber": "SRV-RACK-001",
    "buildingName": "Building 1",
    "deviceType": "RACK",
    "totalShelfPositions": 42,
    "imageUrl": "https://example.com/image.jpg",
    "isDeleted": false
  }
}
```

---

### 3. Get All Devices (with Pagination & Filtering)
**GET** `/api/devices?page=0&size=10&deviceType=RACK&buildingName=Building1&isDeleted=false`

Retrieves all devices with pagination and optional filters.

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10, max: 100) - Page size
- `deviceType` (optional) - Filter by device type
- `buildingName` (optional) - Filter by building name
- `isDeleted` (optional, default: false) - Include/exclude deleted devices

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "deviceId": "uuid-1",
        "deviceName": "Server Rack A1",
        "partNumber": "SRV-RACK-001",
        "buildingName": "Building 1",
        "deviceType": "RACK",
        "totalShelfPositions": 42,
        "imageUrl": "https://example.com/image.jpg",
        "isDeleted": false
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "isFirst": true,
    "isLast": false,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

---

### 4. Update Device
**PUT** `/api/devices/{deviceId}`

Updates an existing device. Only provided fields will be updated.

**Request Body:**
```json
{
  "deviceName": "Updated Server Rack A1",
  "imageUrl": "https://example.com/new-image.jpg"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Device updated successfully",
  "data": {
    "deviceId": "uuid-here",
    "deviceName": "Updated Server Rack A1",
    "partNumber": "SRV-RACK-001",
    "buildingName": "Building 1",
    "deviceType": "RACK",
    "totalShelfPositions": 42,
    "imageUrl": "https://example.com/new-image.jpg",
    "isDeleted": false
  }
}
```

---

### 5. Soft Delete Device
**DELETE** `/api/devices/{deviceId}`

Soft deletes a device (sets isDeleted to true).

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Device deleted successfully"
}
```

---

### 6. Search Devices
**GET** `/api/devices/search?query=rack`

Searches devices by device name or part number.

**Query Parameters:**
- `query` (required) - Search term

**Response (200 OK):**
```json
{
  "success": true,
  "count": 5,
  "data": [
    {
      "deviceId": "uuid-1",
      "deviceName": "Server Rack A1",
      "partNumber": "SRV-RACK-001",
      "buildingName": "Building 1",
      "deviceType": "RACK",
      "totalShelfPositions": 42,
      "imageUrl": "https://example.com/image.jpg",
      "isDeleted": false
    }
  ]
}
```

---

### 7. Get Devices by Building
**GET** `/api/devices/building/{buildingName}`

Retrieves all devices in a specific building.

**Response (200 OK):**
```json
{
  "success": true,
  "count": 15,
  "data": [
    {
      "deviceId": "uuid-1",
      "deviceName": "Server Rack A1",
      "partNumber": "SRV-RACK-001",
      "buildingName": "Building 1",
      "deviceType": "RACK",
      "totalShelfPositions": 42,
      "imageUrl": "https://example.com/image.jpg",
      "isDeleted": false
    }
  ]
}
```

---

### 8. Get Devices by Type
**GET** `/api/devices/type/{deviceType}`

Retrieves all devices of a specific type.

**Response (200 OK):**
```json
{
  "success": true,
  "count": 20,
  "data": [
    {
      "deviceId": "uuid-1",
      "deviceName": "Server Rack A1",
      "partNumber": "SRV-RACK-001",
      "buildingName": "Building 1",
      "deviceType": "RACK",
      "totalShelfPositions": 42,
      "imageUrl": "https://example.com/image.jpg",
      "isDeleted": false
    }
  ]
}
```

---

### 9. Restore Deleted Device
**PATCH** `/api/devices/{deviceId}/restore`

Restores a soft-deleted device.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Device restored successfully",
  "data": {
    "deviceId": "uuid-here",
    "deviceName": "Server Rack A1",
    "partNumber": "SRV-RACK-001",
    "buildingName": "Building 1",
    "deviceType": "RACK",
    "totalShelfPositions": 42,
    "imageUrl": "https://example.com/image.jpg",
    "isDeleted": false
  }
}
```

---

### 10. Get Device Statistics
**GET** `/api/devices/stats`

Retrieves comprehensive statistics about all devices.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalDevices": 100,
    "activeDevices": 85,
    "deletedDevices": 15,
    "deviceTypes": ["RACK", "SHELF", "CABINET"],
    "buildings": ["Building 1", "Building 2", "Building 3"],
    "countByType": {
      "RACK": 50,
      "SHELF": 30,
      "CABINET": 5
    },
    "countByBuilding": {
      "Building 1": 40,
      "Building 2": 30,
      "Building 3": 15
    }
  }
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2026-02-26T10:52:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Device name is required",
  "path": "/api/devices"
}
```

### 404 Not Found
```json
{
  "timestamp": "2026-02-26T10:52:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Device not found with ID: uuid-here",
  "path": "/api/devices/uuid-here"
}
```

---

## Validation Rules

### Create Device
- `deviceName`: Required, non-empty string
- `partNumber`: Required, non-empty string
- `buildingName`: Required, non-empty string
- `deviceType`: Required, non-empty string
- `totalShelfPositions`: Required, must be > 0
- `imageUrl`: Optional string

### Update Device
- All fields are optional
- If provided, fields cannot be empty
- `totalShelfPositions` must be > 0 if provided

---

## Database Schema (Neo4j)

### Device Node
```cypher
(d:Device {
  deviceId: String,           // UUID
  deviceName: String,
  partNumber: String,
  buildingName: String,
  deviceType: String,
  totalShelfPositions: Integer,
  imageUrl: String,
  isDeleted: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime
})
```

### Relationships
```cypher
(Device)-[:HAS_POSITION]->(ShelfPosition)
```

When a device is created, shelf positions are automatically created and linked.

---

## Example Usage with cURL

### Create a Device
```bash
curl -X POST http://localhost:8080/api/devices \
  -H "Content-Type: application/json" \
  -d '{
    "deviceName": "Server Rack A1",
    "partNumber": "SRV-RACK-001",
    "buildingName": "Building 1",
    "deviceType": "RACK",
    "totalShelfPositions": 42,
    "imageUrl": "https://example.com/image.jpg"
  }'
```

### Get All Devices with Filters
```bash
curl "http://localhost:8080/api/devices?page=0&size=10&deviceType=RACK&buildingName=Building1"
```

### Update a Device
```bash
curl -X PUT http://localhost:8080/api/devices/{deviceId} \
  -H "Content-Type: application/json" \
  -d '{
    "deviceName": "Updated Name",
    "imageUrl": "https://example.com/new-image.jpg"
  }'
```

### Delete a Device
```bash
curl -X DELETE http://localhost:8080/api/devices/{deviceId}
```

### Search Devices
```bash
curl "http://localhost:8080/api/devices/search?query=rack"
```

### Get Statistics
```bash
curl http://localhost:8080/api/devices/stats
```

---

## Notes
- All timestamps are in ISO-8601 format
- Pagination page numbers start at 0
- Maximum page size is 100
- Soft delete preserves data; use restore endpoint to recover deleted devices
- When creating a device, shelf positions are automatically created based on `totalShelfPositions`

