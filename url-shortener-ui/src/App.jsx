import React, { useState } from 'react';
import { Link2, Scissors, Copy, Check, ExternalLink, ArrowRight, Zap, Shield, BarChart3 } from 'lucide-react';
import axios from 'axios';
import { motion, AnimatePresence } from 'framer-motion';

const API_BASE_URL = 'http://localhost:8080/api/v1';

function App() {
  const [longUrl, setLongUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!longUrl) return;

    setLoading(true);
    setError('');
    setShortUrl('');

    try {
      const response = await axios.post(`${API_BASE_URL}/shorten`, { longUrl });
      setShortUrl(response.data.shortUrl);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to shorten URL. Is the backend running?');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(shortUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="container">
      <nav className="nav">
        <div className="logo">
          <Link2 className="gradient-text" />
          <span>Snap<span className="gradient-text">Link</span></span>
        </div>
        <div style={{ display: 'flex', gap: '2rem', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
          <span>Features</span>
          <span>Pricing</span>
          <span>Analytics</span>
        </div>
      </nav>

      <main>
        <section className="hero">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
          >
            <h1>Shorten Your <span className="gradient-text">Long Links</span> Instantly</h1>
            <p>A fast, secure, and modern URL shortener built for scale. Track your links with real-time analytics.</p>
          </motion.div>
        </section>

        <motion.div 
          className="glass-card"
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.2 }}
        >
          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <input
                type="url"
                placeholder="Paste your long URL here..."
                value={longUrl}
                onChange={(e) => setLongUrl(e.target.value)}
                required
              />
              <button type="submit" className="btn-primary" disabled={loading}>
                {loading ? 'Shortening...' : (
                  <>
                    <Scissors size={18} />
                    Shorten
                  </>
                )}
              </button>
            </div>
          </form>

          {error && (
            <motion.p 
              initial={{ opacity: 0 }} 
              animate={{ opacity: 1 }}
              style={{ color: 'var(--error)', marginTop: '1rem', textAlign: 'center', fontSize: '0.9rem' }}
            >
              {error}
            </motion.p>
          )}

          <AnimatePresence>
            {shortUrl && (
              <motion.div 
                className="result-card"
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                exit={{ opacity: 0, height: 0 }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                  <Zap size={18} style={{ color: 'var(--primary)' }} />
                  <a href={shortUrl} target="_blank" rel="noopener noreferrer" className="short-url">
                    {shortUrl}
                  </a>
                </div>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  <button className="copy-btn" onClick={copyToClipboard} title="Copy to clipboard">
                    {copied ? <Check size={18} style={{ color: 'var(--success)' }} /> : <Copy size={18} />}
                  </button>
                  <a href={shortUrl} target="_blank" rel="noopener noreferrer" className="copy-btn" title="Open link">
                    <ExternalLink size={18} />
                  </a>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </motion.div>

        <section style={{ marginTop: '6rem', display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '2rem' }}>
          {[
            { icon: <Zap />, title: 'Lightning Fast', desc: 'Optimized with Redis for sub-millisecond redirections.' },
            { icon: <Shield />, title: 'Secure & Reliable', desc: 'Built with industry-standard security and high availability.' },
            { icon: <BarChart3 />, title: 'Real-time Stats', desc: 'Detailed analytics for every click on your shortened links.' }
          ].map((feature, i) => (
            <motion.div 
              key={i}
              className="glass-card"
              style={{ padding: '2rem' }}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.1 }}
            >
              <div style={{ color: 'var(--primary)', marginBottom: '1rem' }}>{feature.icon}</div>
              <h3 style={{ marginBottom: '0.75rem' }}>{feature.title}</h3>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', lineHeight: '1.6' }}>{feature.desc}</p>
            </motion.div>
          ))}
        </section>
      </main>

      <footer className="footer">
        <p>© 2026 SnapLink URL Shortener. All rights reserved.</p>
        <div style={{ marginTop: '1rem', display: 'flex', justifyContent: 'center', gap: '1.5rem' }}>
          <span>Privacy</span>
          <span>Terms</span>
          <span>Contact</span>
        </div>
      </footer>
    </div>
  );
}

export default App;
