import React from 'react'

export default function DslEditor({ value, onChange }) {
    const handleChange = (e) => {
        onChange(e.target.value)
    }

    return (
        <div className="dsl-editor-wrapper">
      <textarea
          className="dsl-editor-textarea"
          value={value}
          onChange={handleChange}
          spellCheck="false"
      />
        </div>
    )
}
