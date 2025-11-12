## Notes

```bash
podman login docker.io
podman build -t sammidev/demo-time:1.0.0 .
podman push sammidev/demo-time:1.0.0

# tes postgres port forward
kubectl port-forward svc/demo-time-db 5433:5432

# Perintah ini akan terus berjalan sampai update selesai
kubectl rollout status deployment/demo-time-app

# Kembali ke revisi (konfigurasi) sebelumnya
kubectl rollout undo deployment/demo-time-app
```

### 1\. 📬 POST /api/events (Membuat Event Baru)

Ini adalah perintah `POST` yang mengirim *body* JSON.

**Contoh A: Mengirim waktu dengan Timezone Jakarta (+07:00)**
Perhatikan `eventTime` menggunakan offset `+07:00`.

```bash
curl -X POST http://localhost:8080/api/events \
-H "Content-Type: application/json" \
-d '{
    "name": "Rapat Tim di Jakarta",
    "eventTime": "2025-11-20T10:00:00+07:00"
}'
```

* **Respons Server (JSON):** Akan menampilkan `eventTime` yang sudah dikonversi ke UTC `...Z`.
  ```json
  {
    "id": 1,
    "name": "Rapat Tim di Jakarta",
    "eventTime": "2025-11-20T03:00:00Z", 
    "createdAt": "..."
  }
  ```

**Contoh B: Mengirim waktu dalam format UTC (Z)**
Ini adalah *best practice* jika *client* sudah tahu cara mengkonversi ke UTC.

```bash
curl -X POST http://localhost:8080/api/events \
-H "Content-Type: application/json" \
-d '{
    "name": "Rilis Sistem (Global)",
    "eventTime": "2025-11-21T14:30:00Z"
}'
```

-----

### 2\. 🔎 GET /api/events/{id} (Mendapatkan Detail Event)

Ini adalah perintah `GET` sederhana. Mari kita asumsikan kita ingin mengambil event dengan `id=1` (dari perintah POST pertama).

```bash
curl -X GET http://localhost:8080/api/events/1
```

* **Respons Server (JSON):**
  ```json
  {
    "id": 1,
    "name": "Rapat Tim di Jakarta",
    "eventTime": "2025-11-20T03:00:00Z",
    "createdAt": "..."
  }
  ```

-----

### 3\. 🗓️ GET /api/events/search (Mencari Event dalam Range)

Ini adalah perintah `GET` dengan *query parameters*.

> **PENTING:** Anda **harus** membungkus URL dengan tanda kutip (`"..."`) karena karakter `&` dan `?` adalah karakter spesial di terminal.

**Contoh: Mencari semua event pada tanggal 20 November 2025 (UTC)**

* `start` = `2025-11-20T00:00:00Z`
* `end` = `2025-11-20T23:59:59Z`

<!-- end list -->

```bash
curl -X GET "http://localhost:8080/api/events/search?start=2025-11-20T00:00:00Z&end=2025-11-20T23:59:59Z"
```

* **Respons Server (JSON):** Akan berupa *array* dari event yang cocok.
  ```json
  [
    {
      "id": 1,
      "name": "Rapat Tim di Jakarta",
      "eventTime": "2025-11-20T03:00:00Z",
      "createdAt": "..."
    }
  ]
  ```

-----

> **⭐ Tips Pro:**
> Jika Anda memiliki `jq` (sebuah JSON *processor*), Anda bisa menambahkan `| jq` di akhir perintah `curl` Anda untuk mendapatkan *output* JSON yang rapi dan berwarna.
>
> **Contoh:**
> `curl -X GET http://localhost:8080/api/events/1 | jq`
