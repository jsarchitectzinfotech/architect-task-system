# Load environment variables from the repo root .env and run the Spring Boot backend.
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$envFile = Join-Path $scriptDir ".env"

if (-Not (Test-Path $envFile)) {
    Write-Error ".env file not found at $envFile"
    exit 1
}

Write-Host "Loading environment from $envFile"
Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith("#")) {
        $parts = $line -split "=", 2
        if ($parts.Count -eq 2) {
            $name = $parts[0].Trim()
            $value = $parts[1].Trim()
            Write-Host "Setting $name"
            Set-Item -Path "Env:$name" -Value $value
        }
    }
}

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error "Maven executable not found. Install Maven or add it to PATH."
    exit 1
}

Set-Location (Join-Path $scriptDir 'backend')
Write-Host "Starting backend in $(Get-Location)"
mvn spring-boot:run