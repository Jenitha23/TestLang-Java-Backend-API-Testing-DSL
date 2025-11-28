import React, { useState } from 'react'
import axios from 'axios'
import './index.css'

const App = () => {
    const [code, setCode] = useState(`config {
  base_url = "http://localhost:8080";
  header "X-App" = "TestLangDemo";
  header "Content-Type" = "application/json";
}

let userId = 42;
let adminUsername = "admin";
let adminPassword = "1234";

test UserAuthentication {
  POST "/api/login" {
    body = "{\\"username\\": \\"$adminUsername\\", \\"password\\": \\"$adminPassword\\"}";
  }
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "token";
}

test GetUserProfile {
  GET "/api/users/$userId";
  expect status = 200;
  expect body contains "\\"id\\":$userId";
}

test UpdateUserRole {
  PUT "/api/users/$userId" {
    body = "{\\"role\\":\\"ADMIN\\"}";
  }
  expect status = 200;
  expect header "X-App" = "TestLangDemo";
  expect body contains "\\"updated\\":true";
}`)

    const [validationResult, setValidationResult] = useState(null)
    const [testResults, setTestResults] = useState(null)
    const [loading, setLoading] = useState(false)
    const [activeTab, setActiveTab] = useState('editor')

    const handleValidate = async () => {
        setLoading(true)
        try {
            const response = await axios.post('/api/validate', { source: code })
            setValidationResult(response.data)
            setTestResults(null)
            setActiveTab('results')
        } catch (error) {
            if (error.response) {
                setValidationResult(error.response.data)
            } else {
                setValidationResult({
                    success: false,
                    errors: [{ message: 'Network error: Unable to reach backend server' }]
                })
            }
            setTestResults(null)
            setActiveTab('results')
        } finally {
            setLoading(false)
        }
    }

    const handleRunTests = async () => {
        setLoading(true)
        try {
            const response = await axios.post('/api/run', { source: code })
            setTestResults(response.data)
            setValidationResult(null)
            setActiveTab('results')
        } catch (error) {
            if (error.response) {
                setTestResults(error.response.data)
            } else {
                setTestResults({
                    summary: 'Network error: Unable to reach backend server',
                    tests: []
                })
            }
            setValidationResult(null)
            setActiveTab('results')
        } finally {
            setLoading(false)
        }
    }

    const loadExample = () => {
        setCode(`config {
  base_url = "http://localhost:8080";
  header "Content-Type" = "application/json";
  header "X-App" = "TestLangDemo";
}

let userId = 42;
let newRole = "ADMIN";

test UserLogin {
  POST "/api/login" {
    body = "{\\"username\\": \\"admin\\", \\"password\\": \\"1234\\"}";
  }
  expect status = 200;
  expect header "Content-Type" contains "json";
  expect body contains "token";
}

test GetUserProfile {
  GET "/api/users/$userId";
  expect status = 200;
  expect body contains "\\"id\\":$userId";
}

test UpdateUserRole {
  PUT "/api/users/$userId" {
    body = "{\\"role\\":\\"$newRole\\"}";
  }
  expect status = 200;
  expect header "X-App" = "TestLangDemo";
  expect body contains "\\"updated\\":true";
  expect body contains "\\"role\\":\\"$newRole\\"";
}`)
    }

    const clearResults = () => {
        setValidationResult(null)
        setTestResults(null)
    }

    const totalTests = testResults?.tests?.length || 0
    const passedTests = testResults?.tests?.filter(t => t.status === 'PASSED').length || 0
    const failedTests = totalTests - passedTests

    return (
        <div className="app">
            <header className="header">
                <div className="header-content">
                    <div className="logo">
                        <span className="logo-icon">⚡</span>
                        <span className="text-gradient">TestLang</span>
                        <span style={{ fontSize: '0.875rem', color: 'var(--text-muted)', marginLeft: 'var(--space-sm)' }}>
              API Testing DSL
            </span>
                    </div>
                    <div className="btn-group">
                        <button className="btn btn-secondary" onClick={loadExample}>
                            📋 Load Example
                        </button>
                        <button className="btn btn-secondary" onClick={clearResults}>
                            🗑️ Clear Results
                        </button>
                    </div>
                </div>
            </header>

            <div className="container">
                {/* Stats Overview */}
                {(testResults || validationResult) && (
                    <div className="stats-container fade-in">
                        <div className="stat-card">
                            <div className="stat-value">{totalTests}</div>
                            <div className="stat-label">Total Tests</div>
                        </div>
                        <div className="stat-card">
                            <div className="stat-value" style={{ color: 'var(--success)' }}>{passedTests}</div>
                            <div className="stat-label">Passed</div>
                        </div>
                        <div className="stat-card">
                            <div className="stat-value" style={{ color: failedTests > 0 ? 'var(--danger)' : 'var(--text-muted)' }}>
                                {failedTests}
                            </div>
                            <div className="stat-label">Failed</div>
                        </div>
                        <div className="stat-card">
                            <div className="stat-value" style={{ color: 'var(--primary)' }}>
                                {totalTests > 0 ? Math.round((passedTests / totalTests) * 100) : 0}%
                            </div>
                            <div className="stat-label">Success Rate</div>
                        </div>
                    </div>
                )}

                {/* Tabs */}
                <div className="tabs">
                    <button
                        className={`tab ${activeTab === 'editor' ? 'active' : ''}`}
                        onClick={() => setActiveTab('editor')}
                    >
                        📝 Editor
                    </button>
                    <button
                        className={`tab ${activeTab === 'results' ? 'active' : ''}`}
                        onClick={() => setActiveTab('results')}
                        disabled={!validationResult && !testResults}
                    >
                        📊 Results
                        {(validationResult || testResults) && ' ●'}
                    </button>
                </div>

                {/* Editor Tab */}
                {activeTab === 'editor' && (
                    <div className="card editor-container fade-in">
                        <div className="card-header">
                            <div className="card-title">
                                <span>🔧</span>
                                TestLang DSL Editor
                            </div>
                            <div className="btn-group">
                                <button
                                    className="btn btn-primary"
                                    onClick={handleValidate}
                                    disabled={loading}
                                >
                                    {loading ? '⏳' : '✓'} Validate Syntax
                                </button>
                                <button
                                    className="btn btn-success"
                                    onClick={handleRunTests}
                                    disabled={loading}
                                >
                                    {loading ? '⏳' : '🚀'} Run Tests
                                </button>
                            </div>
                        </div>
                        <textarea
                            className="editor"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            placeholder="Write your TestLang DSL code here...&#10;&#10;Example:&#10;config {&#10;  base_url = &quot;http://localhost:8080&quot;;&#10;  header &quot;Content-Type&quot; = &quot;application/json&quot;;&#10;}&#10;&#10;test MyTest {&#10;  GET &quot;/api/users/1&quot;;&#10;  expect status = 200;&#10;}"
                            spellCheck="false"
                        />
                    </div>
                )}

                {/* Results Tab */}
                {activeTab === 'results' && (
                    <div className="card results-container fade-in">
                        <div className="card-header">
                            <div className="card-title">
                                <span>📊</span>
                                Execution Results
                            </div>
                            <div className="btn-group">
                                <button className="btn btn-secondary" onClick={() => setActiveTab('editor')}>
                                    ← Back to Editor
                                </button>
                            </div>
                        </div>
                        <div className="card-content">
                            {loading && (
                                <div className="loading">
                                    <div className="loading-spinner"></div>
                                    Processing your request...
                                </div>
                            )}

                            {!loading && validationResult && (
                                <div className={`validation-result ${validationResult.success ? 'validation-success' : 'validation-error'}`}>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-sm)', marginBottom: 'var(--space-sm)' }}>
                                        <strong style={{ fontSize: '1.1rem' }}>
                                            {validationResult.success ? '✅ Syntax Validation Passed' : '❌ Syntax Validation Failed'}
                                        </strong>
                                    </div>
                                    {validationResult.errors && validationResult.errors.map((error, index) => (
                                        <div key={index} style={{
                                            marginTop: 'var(--space-sm)',
                                            padding: 'var(--space-sm)',
                                            background: 'rgba(0,0,0,0.2)',
                                            borderRadius: 'var(--radius-sm)'
                                        }}>
                                            <div style={{ fontFamily: 'var(--font-mono)', fontSize: '0.875rem' }}>
                                                {error.line > 0 && `Line ${error.line}`}{error.column > 0 && `:${error.column}`}
                                                {error.line > 0 && ' - '}
                                                {error.message}
                                            </div>
                                        </div>
                                    ))}
                                    {validationResult.success && (
                                        <div style={{ marginTop: 'var(--space-sm)', color: 'var(--text-secondary)' }}>
                                            Your TestLang DSL code is syntactically correct and ready to run!
                                        </div>
                                    )}
                                </div>
                            )}

                            {!loading && testResults && (
                                <div className="test-results">
                                    <div className="test-summary">
                                        {testResults.summary}
                                    </div>
                                    <div className="test-list">
                                        {testResults.tests && testResults.tests.map((test, index) => (
                                            <div
                                                key={index}
                                                className={`test-item ${test.status === 'PASSED' ? 'test-passed' : 'test-failed'}`}
                                            >
                                                <div className="test-info">
                                                    <div className="test-name">{test.name}</div>
                                                    <div className="test-meta">
                                                        <span className="test-duration">{test.durationMs}ms</span>
                                                        {test.errorMessage && (
                                                            <span className="error-message">{test.errorMessage}</span>
                                                        )}
                                                    </div>
                                                </div>
                                                <span className={`test-status ${test.status === 'PASSED' ? 'status-passed' : 'status-failed'}`}>
                          {test.status}
                        </span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}

                            {!loading && !validationResult && !testResults && (
                                <div className="text-center text-muted" style={{ padding: 'var(--space-xl)' }}>
                                    <div style={{ fontSize: '3rem', marginBottom: 'var(--space-md)' }}>📊</div>
                                    <h3 style={{ marginBottom: 'var(--space-sm)' }}>No Results Yet</h3>
                                    <p>Run validation or execute tests to see results here.</p>
                                </div>
                            )}
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default App