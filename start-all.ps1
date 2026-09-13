Write-Host "Starting CousinInTheCity Multi-Agent Backend..." -ForegroundColor Cyan

# 1. Start Docker Services (Postgres & Redis)
Write-Host "Starting Docker containers..." -ForegroundColor Yellow
docker-compose up -d

Write-Host "Wait 5 seconds for databases to initialize..." -ForegroundColor DarkGray
Start-Sleep -Seconds 5

# 2. Check for Ollama (Required for RAG Embeddings)
Write-Host "Checking if Ollama is running (required for Vector Embeddings)..." -ForegroundColor Yellow
$ollamaRunning = Test-NetConnection -ComputerName localhost -Port 11434 -InformationLevel Quiet
if (-not $ollamaRunning) {
    Write-Host "⚠️ WARNING: Ollama is NOT running on port 11434!" -ForegroundColor Red
    Write-Host "The Orchestrator requires Ollama (nomic-embed-text) for RAG embeddings." -ForegroundColor Red
    Write-Host "Please start the Ollama desktop app before proceeding." -ForegroundColor Red
    Start-Sleep -Seconds 5
} else {
    Write-Host "✅ Ollama is running." -ForegroundColor Green
}

# 3. Build everything first (to ensure no compile errors while booting)
Write-Host "Compiling all modules..." -ForegroundColor Yellow
./gradlew build -x test

# 4. Start the MCP Servers (Background)
Write-Host "Booting MCP Travel (Port 8081)..." -ForegroundColor Green
Start-Process -FilePath "./gradlew" -ArgumentList ":mcp-travel:bootRun" -NoNewWindow -PassThru

Write-Host "Booting MCP Accommodation (Port 8082)..." -ForegroundColor Green
Start-Process -FilePath "./gradlew" -ArgumentList ":mcp-accommodation:bootRun" -NoNewWindow -PassThru

Write-Host "Booting MCP Finance (Port 8083)..." -ForegroundColor Green
Start-Process -FilePath "./gradlew" -ArgumentList ":mcp-finance:bootRun" -NoNewWindow -PassThru

Write-Host "Booting MCP Location (Port 8084)..." -ForegroundColor Green
Start-Process -FilePath "./gradlew" -ArgumentList ":mcp-location:bootRun" -NoNewWindow -PassThru

Write-Host "Wait 15 seconds for MCP servers to boot..." -ForegroundColor DarkGray
Start-Sleep -Seconds 15

# 5. Start the Agent Orchestrator (Foreground)
Write-Host "Booting Agent Orchestrator (Port 8080)..." -ForegroundColor Magenta
Write-Host "=====================================================" -ForegroundColor White
Write-Host "Once this boots, open your browser to: http://localhost:8080" -ForegroundColor White
Write-Host "=====================================================" -ForegroundColor White
./gradlew :agent-orchestrator:bootRun
