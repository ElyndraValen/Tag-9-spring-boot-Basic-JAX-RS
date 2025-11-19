#!/bin/bash

# Spring Boot Basic Tag 9 - API Test Script
# Testet alle JAX-RS Endpoints

BASE_URL="http://localhost:8080/api/persons"

echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║   Spring Boot Basic Tag 9 - JAX-RS API Test                  ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""

# Test 1: GET alle Personen
echo "📍 Test 1: GET /api/persons (alle Personen)"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL" | jq .
echo ""
echo ""

# Test 2: GET Person by ID
echo "📍 Test 2: GET /api/persons/1 (Person mit ID 1)"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL/1" | jq .
echo ""
echo ""

# Test 3: GET Person not found (404)
echo "📍 Test 3: GET /api/persons/999 (Person nicht gefunden - 404)"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL/999" | jq .
echo ""
echo ""

# Test 4: POST neue Person
echo "📍 Test 4: POST /api/persons (neue Person erstellen)"
echo "----------------------------------------"
curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "John",
    "lastname": "Doe",
    "email": "john.doe@example.com"
  }' | jq .
echo ""
echo ""

# Test 5: PUT Person aktualisieren
echo "📍 Test 5: PUT /api/persons/1 (Person aktualisieren)"
echo "----------------------------------------"
curl -s -X PUT "$BASE_URL/1" \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "Updated",
    "lastname": "Name",
    "email": "updated@example.com"
  }' | jq .
echo ""
echo ""

# Test 6: GET Search by firstname
echo "📍 Test 6: GET /api/persons/search?firstname=Max (Suche nach Vorname)"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL/search?firstname=Max" | jq .
echo ""
echo ""

# Test 7: GET Search with pagination
echo "📍 Test 7: GET /api/persons/search?page=0&size=2 (Pagination)"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL/search?page=0&size=2" | jq .
echo ""
echo ""

# Test 8: DELETE Person
echo "📍 Test 8: DELETE /api/persons/5 (Person löschen)"
echo "----------------------------------------"
curl -s -X DELETE "$BASE_URL/5" -w "HTTP Status: %{http_code}\n"
echo ""
echo ""

# Test 9: GET flexible (Content Negotiation)
echo "📍 Test 9: GET /api/persons/flexible (Content Negotiation - JSON)"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL/flexible" -H "Accept: application/json" | jq .
echo ""
echo ""

echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║   Alle Tests abgeschlossen! ✅                                ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
