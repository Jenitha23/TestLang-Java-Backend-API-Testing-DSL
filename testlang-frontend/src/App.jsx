import React, { useState } from 'react'
import axios from 'axios'
import DslEditor from './components/DslEditor'
import Toolbar from './components/Toolbar'
import ResultsPanel from './components/ResultsPanel'

const DEFAULT_EXAMPLE = `// Sample TestLang++ DSL

let user = "admin";
let id = 42;
let password = "1234";

test Login {
  DESCRIPTION "Login should return a token for valid credentials"
  Active : True

  POST "/api/login" {
    body = "{ \\"username\\": \\"$user\\", \\"password\\": \\"$password\\" }";
  };

  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "\\"token\\":";
}

test GetUser {
  DESCRIPTION "Get user by id"
  Active : False

  GET "/api/users/$id";

  expect status = 200;
  expect body contains "\\"id\\":$id";
}
`

export default function App() {
    const [source, setSource] = useState(DEFAULT_EXAMPLE)
    const [isLoading, setIsLoading] = useState(false)
    const [validateResult, setValidateResult] = useState(null)
    const [runResult, setRunResult] = useState(null)
    const [errorMessage, setErrorMessage] = useState(null)

    const handleLoadExample = () => {
        setSource(DEFAULT_EXAMPLE)
        setValidateResult(null)
        setRunResult(null)
        setErrorMessage(null)
    }

    const handleUploadFile = (content) => {
        setSource(content)
        setValidateResult(null)
        setRunResult(null)
        setErrorMessage(null)
    }

    const handleValidate = async () => {
        setIsLoading(true)
        setValidateResult(null)
        setRunResult(null)
        setErrorMessage(null)

        try {
            // FIXED: Path changed from '/api/dsl/validate' to '/api/validate'
            const response = await axios.post('/api/validate', { source })
            setValidateResult(response.data)
        } catch (e) {
            console.error('AxiosError', e)
            setErrorMessage('Failed to connect to backend or process validation. Check console for details.')
        } finally {
            setIsLoading(false)
        }
    }

    const handleRun = async () => {
        setIsLoading(true)
        setValidateResult(null)
        setRunResult(null)
        setErrorMessage(null)

        try {
            // FIXED: Path changed from '/api/dsl/run' to '/api/run'
            const response = await axios.post('/api/run', { source })
            setRunResult(response.data)
        } catch (e) {
            console.error('AxiosError', e)
            setErrorMessage('Failed to connect to backend or run tests. Check console for details.')
        } finally {
            setIsLoading(false)
        }
    }

    // Function to dismiss the error message
    const dismissErrorMessage = () => {
        setErrorMessage(null)
    }

    return (
        <div className="app-root">
            <header className="app-header">
                <div>
                    <h1>TestLang++ DSL Playground</h1>
                    <p>Write, validate, and run your HTTP API tests defined in TestLang++.</p>
                </div>
                <div className="app-header-badge">DSL · JFlex · CUP · JUnit 5</div>
            </header>

            <Toolbar
                onValidate={handleValidate}
                onRun={handleRun}
                onLoadExample={handleLoadExample}
                onUploadFile={handleUploadFile}
                disabled={isLoading}
                isLoading={isLoading}
            />

            {errorMessage && (
                <div className="alert alert-error">
                    <strong>Error:</strong> {errorMessage}
                </div>
            )}

            <main className="app-main">
                <section className="panel panel-editor">
                    <h2>DSL Editor</h2>
                    <DslEditor value={source} onChange={setSource} />
                </section>

                <section className="panel panel-results">
                    <ResultsPanel validateResult={validateResult} runResult={runResult} />
                </section>
            </main>

            <footer className="app-footer">
                <span>Tip:</span> Only tests with <code>Active : True</code> will be executed.
            </footer>
        </div>
    )
}