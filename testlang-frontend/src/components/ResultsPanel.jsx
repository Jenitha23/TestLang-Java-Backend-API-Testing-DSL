import React from 'react'

function ValidationSection({ validateResult }) {
    if (!validateResult) return null

    if (validateResult.success) {
        return (
            <div className="card card-success">
                <h3>Validation</h3>
                <p>No syntax errors. DSL parsed successfully ✅</p>
            </div>
        )
    }

    return (
        <div className="card card-warning">
            <h3>Validation Errors</h3>
            {(!validateResult.errors || validateResult.errors.length === 0) && (
                <p>Validation failed, but no detailed errors were returned.</p>
            )}
            {validateResult.errors && validateResult.errors.length > 0 && (
                <ul className="error-list">
                    {validateResult.errors.map((err, idx) => (
                        <li key={idx}>
                            <code>
                                line {err.line}, col {err.column}
                            </code>{' '}
                            – {err.message}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}

function RunSection({ runResult }) {
    if (!runResult) return null

    return (
        <div className="card card-info">
            <h3>Test Execution</h3>
            {runResult.summary && <p className="summary-text">{runResult.summary}</p>}

            {runResult.tests && runResult.tests.length > 0 ? (
                <table className="results-table">
                    <thead>
                    <tr>
                        <th>Test Name</th>
                        <th>Status</th>
                        <th>Duration (ms)</th>
                        <th>Error</th>
                    </tr>
                    </thead>
                    <tbody>
                    {runResult.tests.map((t, idx) => (
                        <tr key={idx}>
                            <td>{t.name}</td>
                            <td>
                  <span
                      className={
                          t.status === 'PASSED'
                              ? 'status-badge status-pass'
                              : t.status === 'SKIPPED'
                                  ? 'status-badge status-skip'
                                  : 'status-badge status-fail'
                      }
                  >
                    {t.status}
                  </span>
                            </td>
                            <td>{t.durationMs ?? '-'}</td>
                            <td className="error-cell">
                                {t.errorMessage ? <code>{t.errorMessage}</code> : '—'}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p>No test details returned.</p>
            )}
        </div>
    )
}

export default function ResultsPanel({ validateResult, runResult }) {
    if (!validateResult && !runResult) {
        return (
            <div className="card card-empty">
                <h3>Results</h3>
                <p>
                    Use <strong>Validate DSL</strong> to check syntax, or{' '}
                    <strong>Run Tests</strong> to execute active tests.
                </p>
            </div>
        )
    }

    return (
        <div className="results-panel">
            <ValidationSection validateResult={validateResult} />
            <RunSection runResult={runResult} />
        </div>
    )
}
