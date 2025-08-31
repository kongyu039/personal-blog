;(function () {
  layui.use(['form', 'jquery', 'layer', 'table', 'xmSelect', 'upload', 'element'], function () {
    const form = layui.form, layer = layui.layer, $ = layui.jquery, table = layui.table, xmSelect = layui.xmSelect, upload = layui.upload,
      element = layui.element
    const adminTable = tableRender(table)
    table.on('toolbar(admin-table)', function (obj) {
      switch (obj.event) {
        case 'addPost':
          addPost()
          break
        case 'delPost':
          // 获取所有选中的checked
          batchDeletePost(obj)
          break
        case 'crawlPost':
          crawlPost()
          break
      }
    })
    table.on('tool(admin-table)', function (obj) {
      switch (obj.event) {
        case 'rowEdit':
          rowEdit(obj)
          break
        case 'rowDel':
          rowDel(obj)
          break
      }
    })
    const adminForm = form.render(null, 'admin-form')
    formSelectRender($, adminForm, xmSelect)
    adminForm.on('submit(admin-submit)', function (e) {
      adminTable.reloadData({where: e.field})
      return false
    })
    // 2. 监听 switch 变化
    form.on('switch(switchTop)', function (obj) {
      const divEle = obj.elem.nextElementSibling
      divEle.style.pointerEvents = 'none'
      const isTop = obj.elem.checked ? 1 : 0, postId = obj.elem.dataset.id, postTitle = obj.elem.dataset.title
      $.ajax({
        url: ctxUrl + "post/edit-post-top", type: 'post', data: {postId, isTop}, success(res) {
          layer.msg(`[${ postTitle }]置顶状态:${ res.data }`, {time: 1000})
        },
      })
      setTimeout(() => {
        divEle.style.pointerEvents = 'auto'
      }, 1000)
    })
    /** addPost新增文章 */
    function addPost() {
      // noinspection JSCheckFunctionSignatures
      layer.open({
        type: 2, title: '编辑文章', move: false, maxmin: false, area: ['100%', '100%'], content: ctxUrl + 'common/editor.html',
        btn: ['保存', '取消'], yes(index, layero) {submitIframe(index, layero)},
      })
    }
    /** delPost批量删除文章 */
    function batchDeletePost(obj) {
      const {id: filterId} = obj['config']
      const {data} = table.checkStatus(filterId)
      const postIdList = data.map(item => item['id'])
      if (postIdList.length <= 0) return
      // 批量删除 通过idS
      layer.confirm(`是否删除文章${ postIdList }？`, {title: "批量删除文章(回收站)"}, (index, _) => {
        $.ajax({
          url: ctxUrl + 'post/edit-post-del', type: 'post', traditional: true, dataType: 'json', data: {ids: postIdList, flag: true},
          success(res) {
            if (res.code === 200) {
              layer.msg(res.msg, {time: 1000})
              // noinspection JSDeprecatedSymbols
              $('button[lay-filter="admin-submit"]').click()
              layer.close(index)//需要手动关闭
            }
          }, error(res) {
            layer.msg(res['msg'], {time: 1000})
            layer.close(index)//需要手动关闭
          },
        })
      })
    }
    /** crawlPost爬取文章 */
    function crawlPost() {layer.msg('crawlPost 爬取文章：未实现')}
    /** rowEdit行修改事件 */
    function rowEdit(obj) {
      const {data: {id: postId}} = obj
      // noinspection JSCheckFunctionSignatures
      layer.open({
        type: 2, title: '编辑文章', move: false, maxmin: false, area: ['100%', '100%'],
        content: ctxUrl + 'common/editor.html?postId=' + postId, btn: ['保存', '取消'],
        yes(index, layero) {submitIframe(index, layero, postId)},
      })
    }
    /** rowDel行删除事件 */
    function rowDel(obj) {
      const {id: postId, title} = obj['data']
      layer.confirm(`是否删除文章《${ title }》？`, {title: "删除文章(回收站)"}, (index, _) => {
        $.ajax({
          url: ctxUrl + 'post/edit-post-del', type: 'post', dataType: 'json', data: {ids: postId, flag: true}, success(res) {
            if (res.code === 200) {
              layer.msg(res.msg, {time: 1000})
              // noinspection JSDeprecatedSymbols
              $('button[lay-filter="admin-submit"]').click()
              layer.close(index)//需要手动关闭
            }
          },
        })
      })
    }
    /** editor的iframe弹窗提交
     * @param postIndex {number} 弹窗索引
     * @param layero {JQuery} 弹窗Jquery对象
     * @param [postId] {number} 文章Id
     */
    function submitIframe(postIndex, layero, postId) {
      //获取当前引用iframe页面的window对象
      const iframeWin = layero.find('iframe')[0].contentWindow, {adminVditor, titleEle, summaryEle, cover} = iframeWin.editorObj
      if (titleEle.value.trim().length === 0) {
        layer.msg('文章标题不能为空', {icon: 0, time: 1000})
        return
      }
      const titleStr = titleEle.value?.trim().replace(/\s+/g, ''), summaryStr = summaryEle.value?.trim().replace(/\s+/g, '')
      // noinspection JSCheckFunctionSignatures
      const layerPostIndex = layer.open({
        type: 1, resize: false, move: false, title: '提交文章', area: ['630px', 'auto'],
        content: document.querySelector('#post-form').innerHTML, btn: ["提交", "取消"], success(layero) {
          layero.find('.layui-layer-content').css("overflow", "visible")
          form.render(null, 'post-form')
          // 分类和标签渲染
          const {tempXmSelect} = formSelectRender($, form, xmSelect, 'post-form')
          if (postId != null) {
            // 初始化赋值 分类Category
            $.ajax({
              url: ctxUrl + 'category/get-category?postId=' + postId, success(res) {
                layero[0].querySelector('select[name="categoryId"]').value = res.data.id
                // layero.find('select[name="categoryId]').val(res.data.id)
                form.render(null, 'post-form')
              },
            })
            // 初始化赋值 标签Tags
            $.ajax({
              url: ctxUrl + 'tag/get-tags?postId=' + postId, success(res) {
                tempXmSelect.setValue(res.data.map(item => item.id))
              },
            })
          }
          // 封面上传
          // noinspection JSCheckFunctionSignatures
          const coverUpload = upload.render({
            elem: '#cover-upload-preview', field: 'image', url: ctxUrl + 'basic/r2upload', size: 2 * 1024 * 1024, auto: false,
            accept: 'images', acceptMime: 'image/*', bindAction: '#cover-upload-btn',
            choose(obj) {
              console.log("choose", obj)
              obj.preview(function (index, file, result) {
                const $dragChoose = layero.find('.layui-upload-drag>.choose')
                const $dragUpload = layero.find('.layui-upload-drag>.upload')
                $dragChoose.find('.init').addClass('layui-hide')
                $dragChoose.find('img').removeClass('layui-hide')
                $dragChoose.find('img').attr('src', result) // 图片链接（base64）
                $dragUpload.removeClass('layui-hide')
              })
              element.progress('cover-upload-progress', '0%') // 进度条复位
            },
            before(obj) {
              // coverUpload.upload()
              console.log("before", obj)
            },
            progress(n) {
              element.progress('cover-upload-progress', n + '%') // 可配合 layui 进度条元素使用
            }, done(res) {
              if (res.code === 200) {
                layer.msg(res.msg, {time: 1000})
                /** @type {HTMLInputElement} */
                const coverInput = document.querySelector('div[lay-filter="post-form"] input[name="cover"]')
                coverInput.value = res.data
              }
            },
          })
          const uploadDragEle = layero.find('.layui-upload-drag').get(0)
          uploadDragEle.addEventListener('paste', ev => {
            ev.preventDefault()
            if (ev.clipboardData && ev.clipboardData.items && ev.clipboardData.items.length === 1) {
              const item = ev.clipboardData.items[0]
              if (item.kind === 'file' && item.type.startsWith('image/')) {
                let file = item.getAsFile(),
                  reader = new FileReader()
                reader.onload = function (e) {
                  /** @type {string} */
                  const base64 = e.target.result
                  layero.find('.layui-upload-drag>.choose img').attr('src', base64)
                }
                reader.readAsDataURL(file) // 读取文件
                coverUpload.upload([file])
              }
            }
          })
          if (cover != null && cover.length > 0) {
            const $dragChoose = $('.layui-upload-drag>.choose')
            const $dragUpload = $('.layui-upload-drag>.upload')
            $dragChoose.find('.init').addClass('layui-hide')
            $dragChoose.find('img').removeClass('layui-hide')
            $dragChoose.find('img').attr('src', cover) // 图片链接（base64）
            $dragUpload.removeClass('layui-hide')
          }
        }, yes() {
          // 表单提交事件
          form.submit('post-form', function (data) {
            let url = ctxUrl + 'post/add-post'
            if (postId != null) {
              url = ctxUrl + 'post/edit-post'
              data.field['id'] = postId
            }
            data.field['title'] = titleStr
            data.field['summary'] = summaryStr
            data.field['content'] = adminVditor.getValue()
            data.field['htmlContent'] = adminVditor.getHTML()
            const field = data.field // 获取表单字段值
            $.ajax({
              url: url, type: 'post', data: field, dataType: 'json', success(res) {
                if (res.code === 200) {
                  layer.close(layerPostIndex)
                  layer.close(postIndex)
                  // noinspection JSDeprecatedSymbols
                  $('button[lay-filter="admin-submit"]').click()
                }
              }, error(res) {
                layer.msg(res.responseText)
              },
            })
            return false
          })
        },
      })
    }
  })
  /** Form表单渲染 */
  function formSelectRender($, selfForm, xmSelect, formName = "admin-form") {
    const tempObj = {tempForm: null, tempXmSelect: null}
    $.ajax({
      url: ctxUrl + 'category/all-list', async: false, // 设置为同步
      success(res) {
        const {code, data} = res
        if (code === 200) {
          let temp = ''
          if (formName === 'admin-form') {temp += '<option value="" selected>全部分类</option>'}
          data.forEach(item => {temp += `<option value="${ item.id }">${ item.name }</option>`})
          document.querySelector(`.${ formName } .category select[lay-filter="category"]`).innerHTML = temp
          tempObj.tempForm = selfForm.render('select', formName)
        }
      },
    })
    $.ajax({
      url: ctxUrl + 'tag/all-list', async: false, // 设置为同步
      success(res) {
        const {code, data} = res
        if (code === 200) {
          tempObj.tempXmSelect = xmSelect.render({
            el: document.querySelector(`.${ formName } .layui-input-inline.tags`), name: "tagIds", autoRow: true,
            toolbar: {show: true, list: ["ALL", "CLEAR", "REVERSE"]}, data: data.map(item => ({name: item.name, value: item.id})),
          })
        }
      },
    })
    return tempObj
  }
  /** Table表渲染 */
  function tableRender(adminTable) {
    const colsData = [{checkbox: true, fixed: 'left', unresize: true}, {field: 'id', title: 'ID', width: 140, unresize: true},
      {field: 'cover', title: '封面', width: 80, hide: true, unresize: true}, {field: 'title', title: '标题', width: 230, unresize: true},
      {field: 'summary', title: '摘要', unresize: true}, {field: 'category', title: '分类', width: 110, unresize: true},
      {field: 'tags', title: '标签', unresize: true}, {
        field: 'isTop', title: '置顶', width: 80, templet(d) {
          return `<input type="checkbox" name="isTop" lay-skin="switch" lay-filter="switchTop" lay-text="是|否" data-title="${ d.title }" data-id="${ d.id }" ${ d.isTop === 1 ? ' checked' : '' } />`
        }, align: "center", unresize: true,
      }, {title: '创/更时间', width: 180, unresize: true, templet: "更:{{=d.updatedAt}}<br/>创:{{=d.createdAt}}"},
      {title: '操作', width: 120, align: 'center', templet: '#admin-tool', fixed: 'right', unresize: true}]
    return adminTable.render({
      elem: '#admin-table', // 指定原始表格元素选择器（推荐id选择器）
      url: ctxUrl + 'post/get-posts', // 数据接口
      method: 'POST', cols: [colsData], toolbar: '#admin-toolbar', defaultToolbar: false, height: '', response: {statusCode: 200},
      page: true, limit: 10, limits: [10, 15, 20], parseData(res) {
        const datum = res.data['results'].map(item => {
          return {...item, category: item['category'].name, tags: item['tags'].map(item => item.name).join(',')}
        })
        return {code: res.code, msg: res.msg, count: res.data['totalCount'], data: datum}
      },
    })
  }
})()