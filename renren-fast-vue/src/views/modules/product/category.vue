<template>
  <div>
    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandedKey"
      draggable
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            Append
          </el-button>
          <el-button type="text" size="mini" @click="() => edit(data)">
            Edit
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
          >
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="category">
        <el-form-item label="New category name:">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Icon:">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Unit:">
          <el-input
            v-model="category.productUnit"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitData">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  components: {},
  props: {},
  data() {
    return {
      menus: [],
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
        productUnit: "",
        icon: "",
      },
      expandedKey: [],
      dialogTitle: "",
      dialogVisible: false,
      dialogType: "",
      maxLevel: 0,
      updateNodes: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  methods: {
    // get menu in tree shape
    getMenus() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get",
      }).then(({ data }) => {
        this.menus = data.data;
      });
    },

    // add a category
    append(data) {
      this.dialogType = "add";
      this.dialogTitle = "Add Category";
      this.dialogVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1;
      this.category.catId = null;
      this.category.name = "";
      this.category.icon = "";
      this.category.productUnit = "";
      this.category.sort = 0;
      this.category.showStatus = 1;
    },
    addCategory() {
      this.$http({
        url: this.$http.adornUrl("/product/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false),
      }).then(({ data }) => {
        this.$message({
          message: `Product is added`,
          type: "success",
        });
        this.dialogVisible = false;
        this.getMenus();
        this.expandedKey = [this.category.parentCid];
      });
    },

    // edit a category
    edit(data) {
      this.dialogTitle = "Edit Category";
      this.dialogType = "edit";
      this.dialogVisible = true;
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: "get",
      }).then(({ data }) => {
        this.category.name = data.data.name;
        this.category.catId = data.data.catId;
        this.category.icon = data.data.icon;
        this.category.productUnit = data.data.productUnit;
        this.category.parentCid = data.data.parentCid;
        this.category.catLevel = data.data.catLevel;
        this.category.sort = data.data.sort;
        this.category.showStatus = data.data.showStatus;
      });
    },
    editCategory() {
      var { catId, name, icon, productUnit } = this.category;

      this.$http({
        url: this.$http.adornUrl("/product/category/update"),
        method: "post",
        data: this.$http.adornData({ catId, name, icon, productUnit }, false),
      }).then(({ data }) => {
        this.$message({
          message: `Product is updated`,
          type: "success",
        });
        this.dialogVisible = false;
        this.getMenus();
        this.expandedKey = [this.category.catId];
      });
    },

    // helper function for adding and editing catelog
    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      } else if (this.dialogType == "edit") {
        this.editCategory();
      }
    },

    // remove a catelog
    remove(node, data) {
      this.$confirm(
        `Are you sure that you want to delete this product: ${data.name}?`,
        "Warning",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      )
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData([data.catId], false),
          }).then(({ data }) => {
            this.$message({
              message: `Product is deleted`,
              type: "success",
            });
            this.getMenus();
            this.expandedKey = [node.parent.data.catId];
          });
        })
        .catch(() => {});
    },

    /* drag and drop */
    // if the (category) node is droppable
    allowDrop(draggingNode, dropNode, type) {
      this.maxLevel = 0;
      this.nodeLevel(draggingNode.data);
      let depth = this.maxLevel - draggingNode.data.catLevel + 1;
      return type == "inner"
        ? depth + dropNode.level <= 3
        : depth + dropNode.parent.level <= 3;
    },
    // compute the total levels of a node (max of `parent + self + children`)
    nodeLevel(node) {
      if (node.children != null && node.children.length > 0) {
        for (let i = 0; i < node.children.length; i++) {
          if (node.children[i].catLevel > this.maxLevel) {
            this.maxLevel = node.children[i].catLevel;
          }
          this.nodeLevel(node.children[i]);
        }
      }
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      this.updateNodes = [];
      console.log("tree drop: ", draggingNode, dropNode, dropType);
      let parentCid =
        dropType == "inner"
          ? dropNode.data.catId
          : dropNode.parent.data.catId || 0;
      let siblings =
        dropType == "inner" ? dropNode.childNodes : dropNode.parent.childNodes;

      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId == draggingNode.data.catId) {
          // handle the dragging node
          let level = draggingNode.level;
          if (siblings[i].level != draggingNode.level) {
            // dragging node's level changes
            level = siblings[i].level;
            // also need to change children's level
            this.handleDropChildren(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid,
            catLevel: level,
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }
      console.log(this.updateNodes);
      this.$http({
        url: this.$http.adornUrl("/product/category/updateBatch"),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false),
      }).then(({ data }) => {
        this.$message({
          message: `Products are updated`,
          type: "success",
        });
        this.getMenus();
        this.expandedKey = [parentCid];
        this.updateNodes = [];
        this.maxLevel = 0;
      });
    },
    handleDropChildren(node) {
      for (let i = 0; i < node.childNodes.length; i++) {
        var currNode = node.childNodes[i].data;
        this.updateNodes.push({
          catId: currNode.catId,
          catLevel: node.childNodes[i].level,
        });
        this.handleDropChildren(node.childNodes[i]);
      }
    },
  },
  computed: {},
  watch: {},
  created() {
    this.getMenus();
  },
  mounted() {},
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
};
</script>

<style scoped>
</style>