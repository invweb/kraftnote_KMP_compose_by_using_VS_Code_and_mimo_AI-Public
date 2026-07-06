import { chromium } from 'playwright';
import { spawn } from 'child_process';
import http from 'http';

const JAVA_HOME = '/Applications/Android Studio.app/Contents/jbr/Contents/Home';
const BASE_URL = 'http://localhost:8081';
const API_URL = 'http://localhost:8080';
const SCREENSHOT_DIR = 'docs/screenshots';

function httpGet(url) {
  return new Promise((resolve, reject) => {
    http.get(url, (res) => {
      let data = '';
      res.on('data', (c) => (data += c));
      res.on('end', () => resolve(data));
    }).on('error', reject);
  });
}

function httpPost(url, body) {
  return new Promise((resolve, reject) => {
    const data = JSON.stringify(body);
    const req = http.request(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Content-Length': Buffer.byteLength(data) },
    }, (res) => {
      let d = '';
      res.on('data', (c) => (d += c));
      res.on('end', () => resolve(d));
    });
    req.on('error', reject);
    req.write(data);
    req.end();
  });
}

function httpDelete(url) {
  return new Promise((resolve) => {
    const req = http.request(url, { method: 'DELETE' }, () => resolve());
    req.on('error', () => resolve());
    req.end();
  });
}

function waitForPort(port, timeout = 90000) {
  return new Promise((resolve, reject) => {
    const start = Date.now();
    const check = () => {
      const req = http.get(`http://localhost:${port}`, () => resolve());
      req.on('error', () => {
        if (Date.now() - start > timeout) reject(new Error(`Timeout waiting for port ${port}`));
        else setTimeout(check, 1500);
      });
      req.end();
    };
    check();
  });
}

async function seedServer() {
  const pages = [
    {
      id: 'web-1', title: 'Welcome to KraftNote',
      content: '# Welcome\n\nThis is your first page. Start writing in **Markdown**!\n\n## Features\n\n- Create and edit pages\n- Markdown preview\n- Cross-platform sync',
      createdAt: Date.now() - 100000, updatedAt: Date.now() - 100000,
    },
    {
      id: 'web-2', title: 'Meeting Notes',
      content: '## Sprint Planning\n\n- Review backlog\n- Estimate tasks\n- Assign owners\n\n**Next meeting:** Friday 3pm',
      createdAt: Date.now() - 50000, updatedAt: Date.now() - 50000,
    },
    {
      id: 'web-3', title: 'Shopping List',
      content: '- Milk\n- Bread\n- Eggs\n- Coffee\n- Butter',
      createdAt: Date.now() - 10000, updatedAt: Date.now() - 10000,
    },
  ];
  for (const page of pages) {
    await httpPost(`${API_URL}/api/pages`, page);
  }
  console.log('  Server seeded with 3 pages');
}

async function deletePage(id) {
  await httpDelete(`${API_URL}/api/pages/${id}`);
}

async function waitForCanvasContent(page, timeout = 15000) {
  await page.waitForFunction(() => {
    const canvas = document.querySelector('canvas');
    if (!canvas) return false;
    const ctx = canvas.getContext('2d');
    if (!ctx) return false;
    const data = ctx.getImageData(0, 0, canvas.width, canvas.height).data;
    for (let i = 0; i < data.length; i += 4) {
      if (data[i + 3] > 0) return true;
    }
    return false;
  }, { timeout });
}

async function takeScreenshots() {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext({
    viewport: { width: 1280, height: 720 },
    deviceScaleFactor: 2,
  });
  const pg = await context.newPage();

  // --- Screenshot 1: Empty list ---
  console.log('1. Taking empty list screenshot...');
  for (const id of ['web-1', 'web-2', 'web-3']) await deletePage(id);
  await pg.goto(BASE_URL);
  await pg.waitForTimeout(5000);
  await waitForCanvasContent(pg).catch(() => {});
  await pg.screenshot({ path: `${SCREENSHOT_DIR}/web_empty_list.png` });
  console.log('   Done: web_empty_list.png');

  // --- Screenshot 2: One page ---
  console.log('2. Taking one-page screenshot...');
  await seedServer();
  await deletePage('web-2');
  await deletePage('web-3');
  await pg.reload();
  await pg.waitForTimeout(5000);
  await waitForCanvasContent(pg).catch(() => {});
  await pg.screenshot({ path: `${SCREENSHOT_DIR}/web_one_page.png` });
  console.log('   Done: web_one_page.png');

  // --- Screenshot 3: Multiple pages ---
  console.log('3. Taking multiple pages screenshot...');
  await seedServer();
  await pg.reload();
  await pg.waitForTimeout(5000);
  await waitForCanvasContent(pg).catch(() => {});
  await pg.screenshot({ path: `${SCREENSHOT_DIR}/web_pages.png` });
  console.log('   Done: web_pages.png');

  // --- Screenshot 4: Editor empty (click +) ---
  console.log('4. Taking empty editor screenshot...');
  await pg.reload();
  await pg.waitForTimeout(5000);
  await waitForCanvasContent(pg).catch(() => {});
  // Click the + button (top-right area of canvas)
  await pg.mouse.click(1260, 40);
  await pg.waitForTimeout(3000);
  await pg.screenshot({ path: `${SCREENSHOT_DIR}/web_editor_empty.png` });
  console.log('   Done: web_editor_empty.png');

  // --- Screenshot 5: Editor with content (go back, click first page) ---
  console.log('5. Taking editor with content screenshot...');
  // Click back arrow (top-left area)
  await pg.mouse.click(30, 40);
  await pg.waitForTimeout(3000);
  // Click first card
  await pg.mouse.click(640, 130);
  await pg.waitForTimeout(3000);
  await pg.screenshot({ path: `${SCREENSHOT_DIR}/web_editor_content.png` });
  console.log('   Done: web_editor_content.png');

  await browser.close();
}

async function main() {
  console.log('=== Web Screenshot Generator ===\n');

  // Kill existing processes
  const { execSync } = await import('child_process');
  try { execSync('lsof -ti:8080 -ti:8081 2>/dev/null | xargs kill -9 2>/dev/null'); } catch {}

  console.log('Starting server...');
  const server = spawn('./gradlew', [':server:run'], {
    env: { ...process.env, JAVA_HOME },
    stdio: 'pipe',
  });
  server.stderr.on('data', () => {});

  try {
    await waitForPort(8080, 120000);
    console.log('Server is ready on port 8080\n');

    console.log('Starting web dev server...');
    const web = spawn('./gradlew', [':webApp:wasmJsBrowserDevelopmentRun'], {
      env: { ...process.env, JAVA_HOME },
      stdio: 'pipe',
    });
    web.stderr.on('data', () => {});

    await waitForPort(8081, 120000);
    console.log('Web server is ready on port 8081\n');

    await seedServer();
    await takeScreenshots();
    console.log('\n=== All screenshots saved to docs/screenshots/ ===');
  } catch (e) {
    console.error('Error:', e.message);
    process.exit(1);
  } finally {
    server.kill('SIGTERM');
    try { execSync('lsof -ti:8080 -ti:8081 2>/dev/null | xargs kill -9 2>/dev/null'); } catch {}
    process.exit(0);
  }
}

main();
