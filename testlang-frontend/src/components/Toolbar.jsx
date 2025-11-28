import React, { useRef } from 'react'

export default function Toolbar({
                                    onValidate,
                                    onRun,
                                    onLoadExample,
                                    onUploadFile,
                                    disabled,
                                    isLoading
                                }) {
    const fileInputRef = useRef(null)

    const handleUploadClick = () => {
        fileInputRef.current?.click()
    }

    const handleFileChange = (e) => {
        const file = e.target.files?.[0]
        if (!file) return

        const reader = new FileReader()
        reader.onload = () => {
            onUploadFile(String(reader.result))
        }
        reader.readAsText(file)
    }

    return (
        <div className="toolbar">
            <div className="toolbar-left">
                <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={onLoadExample}
                    disabled={disabled}
                >
                    Load Example
                </button>

                <button
                    type="button"
                    className="btn btn-outline"
                    onClick={handleUploadClick}
                    disabled={disabled}
                >
                    Upload .test file
                </button>
                <input
                    ref={fileInputRef}
                    type="file"
                    accept=".test,.txt"
                    style={{ display: 'none' }}
                    onChange={handleFileChange}
                />
            </div>

            <div className="toolbar-right">
                <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={onValidate}
                    disabled={disabled}
                >
                    {isLoading ? 'Validating…' : 'Validate DSL'}
                </button>
                <button
                    type="button"
                    className="btn btn-primary"
                    onClick={onRun}
                    disabled={disabled}
                >
                    {isLoading ? 'Running…' : 'Run Tests'}
                </button>
            </div>
        </div>
    )
}
